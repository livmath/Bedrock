using System;
using System.Collections;
using System.Threading;

class SceneTerrainWorker : PacketHandler
{

    private volatile bool shouldRun = true;

    private readonly int index;
    private readonly WorldScene sceneManager;
    private ChunkFile chunkFile;
    private SceneObserver[] observers;

    private static readonly int DEFAULT_TICK_RATE = 15;
    private long lastTickUpdate;
    private int lastFrameDuration;
    private int tickInterval;

    private Thread thread;

    public SceneTerrainWorker(WorldScene scene, int index)
    {

        SetTickRate(DEFAULT_TICK_RATE);

        sceneManager = scene;
        this.index = index;

        chunkFile = new ChunkFile($"{Settings.SAVE_PATH}/level/worker_{index}_chunks.btf");
    }

    public void SetTickRate(int rate)
    {
        tickInterval = 1000 / rate;
    }

    public int GetTickRate()
    {
        return tickInterval / 1000;
    }

    public void Update()
    {

        bool isTickUpdate = DateTime.Now.Millisecond -lastTickUpdate > tickInterval;

        if (isTickUpdate)
        {

            lastTickUpdate = DateTime.Now.Millisecond;
            HandlePackets();
        }

        lastFrameDuration = (int)(DateTime.Now.Millisecond - lastTickUpdate);

        foreach (SceneObserver observer in observers)
        {

            // this is easier seen by drawing on paper

            int chunkX = (int)Math.Floor(observer.gameObject.transform.position.x / CHUNK_SIZE);
            int chunkY = (int)Math.Floor(observer.gameObject.transform.position.y / CHUNK_SIZE);

            // clients deal with this themselves
            if (observer.isLocal)
            {

                LocalSceneObserver localObserver = (LocalSceneObserver)observer;

                int boundsWidth = chunkX - observer.prevChunkX;
                int boundsHeight = chunkY - observer.prevChunkY;

                int xOffset = chunkX - observer.prevChunkX;
                int yOffset = chunkX - observer.prevChunkY;

                // bounds of collision
                for (int y = 0; y < boundsHeight; y++)
                {
                    for (int x = 0; x < boundsWidth; x++)
                        localObserver.chunks[x + observer.renderDistance * y] = localObserver.chunks[(x - xOffset) + observer.renderDistance * (y - yOffset)];
                }
            }

            // new area bounds
            for (int y = 0; y < observer.renderDistance; y++)
            {

                int layerOffset = 0;
                int layerWidth = observer.renderDistance;

                if (observer.prevChunkY + observer.renderDistance >= y && observer.prevChunkY <= y)
                {
                    layerOffset = Math.Max(observer.prevChunkX, chunkX);
                    layerWidth = Math.Max(0, Math.Min(observer.prevChunkX - chunkX, observer.renderDistance));
                }

                for (int x = layerOffset; x < layerWidth; x++)
                {

                    int cx = chunkX + x;
                    int cy = chunkY + y;

                    if (observer.isLocal)
                    {
                        LocalSceneObserver localObserver = (LocalSceneObserver)observer;
                        localObserver.chunks[x + CHUNK_SIZE * y] = chunkFile.GetChunk(cx, cy);
                    }
                    else
                    {
                        SendPacket((int)SceneWorkerProtocol.GetChunk, new byte[] { (byte)observer.index, (byte)cx, (byte)cy });
                    }
                }
            }
        }
    }

    public override void HandlePacket(int command, DataStream payload)
    {

        int askerId = payload.ReadShort();
        SceneObserver observer = observers[askerId];

        switch ((SceneWorkerProtocol) command) {

            case SceneWorkerProtocol.GetChunk:

                int x = payload.ReadShort();
                int y = payload.ReadShort();

                observer.socket.SendPacket((int) SceneWorkerProtocol.ChunkData, chunkFile.GetChunkEncoded(x, y));
                break;

            case SceneWorkerProtocol.SetTile:

                int chunkX = payload.ReadShort();
                int chunkY = payload.ReadShort();

                int tileId = payload.ReadByte();
                int tileX = payload.ReadByte();
                int tileY = payload.ReadByte();
                int tileZ = payload.ReadByte();

                Chunk chunk = chunkFile.GetChunk(chunkX, chunkY);
                chunk.SetTile(tileId, tileX, tileY, tileZ);

                // at the moment no permission checks, send back 'SetTile' command with answer
                observer.socket.SendPacket((int) SceneWorkerProtocol.SetTile, 1);
                break;

            case SceneWorkerProtocol.TickFrameDuration:

                // send tick time to gauge load. should maybe be average duration.
                sceneManager.SendPacket((int) SceneWorkerProtocol.TickFrameDuration, lastFrameDuration);
                break;

            // finish jobs, free up all resources then send confirmation
            case SceneWorkerProtocol.Shutdown:

                // don't bother waiting for next tick as not accepting more jobs anyway
                HandlePackets();

                // close resources
                chunkFile.Close();
                shouldRun = false;

                sceneManager.SendPacket((int) SceneWorkerProtocol.ShutdownComplete, index);
                break;
        }
    }

    public void Start()
    {

        thread = new Thread(Run);
        thread.Start();
    }

    public void Run()
    {

        while (shouldRun)
            Update();
    }
}