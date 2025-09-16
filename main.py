from fastapi import FastAPI
from api.search_routes import router as search_router
from api.caption_routes import router as caption_router

app = FastAPI()

app.include_router(search_router)
app.include_router(caption_router)

@app.get('/health')
async def health_check():
    return {"status": "ok"}

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host='0.0.0.0', port=8000, reload=True)
