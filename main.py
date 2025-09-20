# main.py
from fastapi import FastAPI
from api.search_routes import router as search_router
from api.caption_routes import router as caption_router

app = FastAPI(title="Snapseek API")

# Mount feature routers
app.include_router(search_router)          # or add prefix/tags inside each router module
app.include_router(caption_router)

@app.get("/health")
async def health():
    return {"status": "ok"}

# For local dev, prefer running: uvicorn main:app --host 127.0.0.1 --port 8000 --reload
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)
