using UnityEditor;
using UnityEngine;

class LocalSceneObserver : SceneObserver
{

    public Chunk[] chunks;

    public LocalSceneObserver() : base()
    {
        chunks = new Chunk[renderDistance *renderDistance];
    }
}