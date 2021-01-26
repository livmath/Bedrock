using UnityEngine;

class SceneObserver: MonoBehaviour
{

    public int index;
    public WorldScene scene;
    public SceneTerrainWorker worker;
    public bool isLocal;

    public readonly IPacketStream socket;

    public int renderDistance = 6;
    public int prevChunkX;
    public int prevChunkY;

    public SceneObserver()
    {
    }

    void Start()
    {

    }

    void Update()
    {

    }

    private void OnDestroy()
    {
        scene.UnregisterObserver(this);
    }
}