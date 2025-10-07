import numpy as np
import json
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List
from models.image_encoder import get_image_encoder

router = APIRouter()

# Load precomputed embeddings and filenames
embeddings = np.load('embeddings.npy')
with open('image_files.json', 'r') as f:
    image_files = json.load(f)

encoder = get_image_encoder()

class SearchRequest(BaseModel):
    text_query: str

class SearchResponse(BaseModel):
    results: List[str]

def cosine_similarity(a, b):
    return np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b))

@router.post('/search', response_model=SearchResponse)
async def search_images(req: SearchRequest):
    try:
        query_embedding = encoder.encode_text(req.text_query)
        sims = [cosine_similarity(query_embedding, emb) for emb in embeddings]
        sorted_indices = np.argsort(sims)[::-1]
        top_k = 5
        top_files = [image_files[i] for i in sorted_indices[:top_k]]
        return SearchResponse(results=top_files)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
