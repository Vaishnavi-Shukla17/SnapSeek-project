from fastapi import APIRouter, HTTPException
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
        raise HTTPException(status_code=500, detail=str(e))
