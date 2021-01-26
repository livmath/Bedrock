using System;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

class WorldScene: PacketHandler{

	private static readonly int MAX_WORKERS = 1; // calculate this in the future
	public SceneTerrainWorker[] workers = new SceneTerrainWorker[MAX_WORKERS];
	private int workersSpawned = 0;

	// i do want to change this to actually determining cost, but it's a quick fill-in
	private int MAGIC_WORK_COST_VALUE = 1; // ms
	private int tickRate = 15;
    private uint tickInterval;

    private int CHUNK_SIZE = 16;

    // keep track of actors moving around
    private readonly List<SceneObserver> observers = new List<SceneObserver>();

	public WorldScene(){
		SpawnWorker();
	}

	public void Update()
	{
		HandlePackets();
    }

	// register player and potential other player like entities
	public SceneObserver RegisterObserver(GameObject actor)
	{

		SceneObserver observer = actor.AddComponent<SceneObserver>();
		observer.scene = this;
		observer.worker = workers[0];

		observers.Add(observer);
		observer.isLocal = false;

		return observer;
	}

	// local means in the same program process
	public LocalSceneObserver RegisterLocalObserver(GameObject actor)
	{

		LocalSceneObserver observer = actor.AddComponent<LocalSceneObserver>();
		observer.scene = this;
		observer.worker = workers[0];

		observers.Add(observer);
		observer.isLocal = true;

		return observer;
	}

	public void UnregisterObserver(SceneObserver observer)
	{
		observers.Remove(observer);
	}

	public override void HandlePacket(int command, DataStream payload){

		switch ((SceneWorkerProtocol) command){

			// stuff sent from workers

			case SceneWorkerProtocol.OffloadWork:

				// find a way of keeping track of. if not enough, spawn another worker
				uint offloadedTimeNeeded = payload.ReadUnsignedInt();

				Broadcast((int) SceneWorkerProtocol.TickFrameDuration);
				break;

			case SceneWorkerProtocol.TickFrameDuration:

				// siphon off jobs

				int workerIndex = payload.ReadByte();

				uint tickFrameDuration = payload.ReadUnsignedInt();
				int jobCount = (int)((tickInterval -tickFrameDuration) /MAGIC_WORK_COST_VALUE);

				// ask worker for packets
				SceneTerrainWorker worker = workers[workerIndex];
				worker.SendPacket((int) SceneWorkerProtocol.TakeWork, jobCount);
				break;

			// read bytes don't care what the packets are, and send to correct worker
			case SceneWorkerProtocol.GiveWork:

				workerIndex = payload.ReadByte();
				uint packetsByteSize = payload.ReadUnsignedInt();

				byte[] bytes = new byte[packetsByteSize];
				payload.ReadFully(bytes);

				workers[workerIndex].SendPacket(0, bytes);
				break;

			case SceneWorkerProtocol.ShutdownComplete:

				// free up worker
				workers[payload.ReadByte()] = null;

				if (--workersSpawned == 0)
					OnCloseFinalise();
				break;
		}
	}

	public void Close(){
		Broadcast((int) SceneWorkerProtocol.Shutdown);
	}

	public void OnCloseFinalise()
	{

	}

	private void Broadcast(int command)
	{

		foreach (SceneTerrainWorker worker in workers)
		{

			if (worker != null)
				worker.SendPacket(command);
		}
	}

	private void SpawnWorker(){

		if(workersSpawned == MAX_WORKERS)
			return;

		try{

			int index = ArrayUtil.FindEmptySlot(workers);
			SceneTerrainWorker worker = new SceneTerrainWorker(this, index);
			workers[index] = worker;

			worker.SetTickRate(tickRate);
			worker.Start();

			workersSpawned++;
		} catch(FileNotFoundException exc){
			Debug.Log("Unable to open file for scene worker!");
		}
	}

	public void DespawnWorker(int index){

		try{
			workers[index].SendPacket((int) SceneWorkerProtocol.Shutdown);
		} catch(IOException ignored){}
	}
}