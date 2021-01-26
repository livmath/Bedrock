enum SceneWorkerProtocol: int
{

    GetChunk,
    ChunkData,
    SetTile,

    Shutdown,
    ShutdownComplete,
    TickFrameDuration,
    TakeWork,
    GiveWork,
    OffloadWork,
    UnreferenceChunk
}