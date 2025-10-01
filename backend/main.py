# main.py
import logging
from fastapi import FastAPI
from api.search_routes import router as search_router
from api.caption_routes import router as caption_router

# Optional: basic logging so model logs are visible
logging.basicConfig(level=logging.INFO)

app = FastAPI(title="Snapseek API")

# Register feature routers
app.include_router(search_router)      # ensure api/search_routes.py defines `router`
app.include_router(caption_router)     # ensure api/caption_routes.py defines `router`

@app.get("/health")
async def health():
    return {"status": "ok"}

# Dev entrypoint (you can also use CLI: uvicorn main:app --host 127.0.0.1 --port 8000)
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)
