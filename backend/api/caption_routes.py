'''from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from models.caption_generator import get_caption_generator

router = APIRouter()

caption_generator = get_caption_generator()

class CaptionRequest(BaseModel):
    image_path: str

class CaptionResponse(BaseModel):
    caption: str

@router.post('/caption', response_model=CaptionResponse)
async def generate_image_caption(req: CaptionRequest):
    try:
        caption = caption_generator.generate_caption(req.image_path)
        return CaptionResponse(caption=caption)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))'''
# api/caption_routes.py
from fastapi import APIRouter
from enum import Enum
from pydantic import BaseModel
from models.caption_generator import get_caption_generator

router = APIRouter()

class Tone(str, Enum):
    funny = "funny"
    playful = "playful"
    professional = "professional"
    romantic = "romantic"

class CaptionRequest(BaseModel):
    image_path: str
    tone: Tone

caption_generator = get_caption_generator()

@router.post("/caption")
def caption(req: CaptionRequest):
    result = caption_generator.generate_caption(image=req.image_path, tone=req.tone.value)
    return {"caption": result}
