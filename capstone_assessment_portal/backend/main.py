from fastapi import Depends, FastAPI
from app.routes.auth_routes import router as auth_router

app = FastAPI()

# routers
app.include_router(auth_router)

@app.get("/")
def home():
    return {"message": "Server started successfully"}
