# Test - Android App

## About
This Android application is designed to fulfill the requirements for Frontend position.

## Libraries

- Jetpack Compose
- Room
- Hilt
- Coil
- Retrofit
- Paging3

## Task Description
### 1. Login Page
Create a login page with fields for users to input their email and password. Only registered users can log in. 
<img src="/assets/login_screen.jpg" width="200">

### 2. Sign Up Page
Provide a page for users to sign up with fields for username, email, password, and role (Admin or Normal User). User data will be saved in local database.
<img src="/assets/register_screen.jpg" width="200">

### 3. Main Page
#### For Normal Users:
- Display data from [this API](https://jsonplaceholder.typicode.com/photos?_page=1&_limit=10)
- Implement Paging3 and Room to fetch data from the API and save it to a local database.
<img src="/assets/photos_screen.jpg" width="200">


#### For Admins:
- Display a list of registered users including id, username, email, and role.
- Provide action buttons for updating/deleting user data. 
- Deleting data requires verification with a password.
<p float="left">
    <img src="/assets/users_screen.jpg" width="200">
    <img src="/assets/delete_user.jpg" width="200">
</p>

