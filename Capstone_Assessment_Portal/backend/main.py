from fastapi import Depends, FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.exceptions.global_exception_handler import register_exception_handlers
from app.routes.auth_routes import router as auth_router
from app.routes.category_routes import router as category_router
from app.routes.quiz_routes import router as quiz_router
from app.routes.question_routes import router as question_router
from app.routes.quiz_attempt_routes import router as quiz_attempt_router

app = FastAPI()

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# custom exception handlers
register_exception_handlers(app)

# routers
app.include_router(auth_router)
app.include_router(category_router)
app.include_router(quiz_router)
app.include_router(question_router)
app.include_router(quiz_attempt_router)

@app.get("/")
def home():
    return {"message": "Server started successfully"}
