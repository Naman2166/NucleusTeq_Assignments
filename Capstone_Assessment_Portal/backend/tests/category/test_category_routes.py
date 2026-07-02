"""
Test cases for Category routes
"""

from bson import ObjectId
from main import app
from app.security.auth import get_current_user, require_admin
from app.services.category_service import CategoryService


def test_get_all_categories(client, mocker):
    """
    Test get all categories
    """

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "naman@gmail.com",
        "role": "student",
    }

    mocker.patch.object(
        CategoryService, 
        "get_all_categories", 
        return_value=[]
    )

    response = client.get("/categories/")

    assert response.status_code == 200

    app.dependency_overrides.clear()



def test_get_category_by_id(client, mocker):
    """
    Test get category by id
    """

    app.dependency_overrides[get_current_user] = lambda: {
        "email": "naman@gmail.com",
        "role": "student",
    }

    category_id = str(ObjectId())

    mocker.patch.object(
        CategoryService,
        "get_category_by_id",
        return_value={
            "id": category_id,
            "name": "Programming",
            "description": "Programming quizzes",
        },
    )

    response = client.get(f"/categories/{category_id}")

    assert response.status_code == 200

    app.dependency_overrides.clear()



def test_create_category(client, mocker):
    """
    Test create category
    """

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    mocker.patch.object(
        CategoryService,
        "create_category",
        return_value={
            "id": str(ObjectId()),
            "name": "Programming",
            "description": "Programming quizzes",
        },
    )

    response = client.post(
        "/categories/",
        json={
            "name": "Programming",
            "description": "Programming quizzes",
        },
    )

    assert response.status_code == 200

    app.dependency_overrides.clear()



def test_update_category(client, mocker):
    """
    Test update category
    """

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    category_id = str(ObjectId())

    mocker.patch.object(
        CategoryService,
        "update_category",
        return_value={
            "id": category_id,
            "name": "Java",
            "description": "Java quizzes",
        },
    )

    response = client.put(
        f"/categories/{category_id}",
        json={
            "name": "Java",
            "description": "Java quizzes",
        },
    )

    assert response.status_code == 200

    app.dependency_overrides.clear()



def test_delete_category(client, mocker):
    """
    Test delete category
    """

    app.dependency_overrides[require_admin] = lambda: {
        "email": "admin@gmail.com",
        "role": "admin",
    }

    category_id = str(ObjectId())

    mocker.patch.object(
        CategoryService,
        "delete_category",
        return_value={
            "message": "Category deleted successfully"
        },
    )

    response = client.delete(
        f"/categories/{category_id}"
    )

    assert response.status_code == 200

    app.dependency_overrides.clear()