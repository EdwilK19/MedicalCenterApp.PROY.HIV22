<?php
    
    require_once 'user.php';
    
    $username = "";
    
    $password = "";
    
    $email = "";
    
    $first_name = "";
    
    $last_name = "";
    
    $identification = "";
    
    $nationality = "";
    
    $birth;
    
    $phone = 0;
    
    if(isset($_POST['username'])){
        
        $username = $_POST['username'];
        
    }
    
    if(isset($_POST['password'])){
        
        $password = $_POST['password'];
        
    }
    
    if(isset($_POST['email'])){
        
        $email = $_POST['email'];
        
    }
    
    if(isset($_POST['first_name'])){
        
        $first_name = $_POST['first_name'];
        
    }
    
    if(isset($_POST['last_name'])){
        
        $last_name = $_POST['last_name'];
        
    }
    
    if(isset($_POST['identification'])){
        
        $identification = $_POST['identification'];
        
    }
    
    if(isset($_POST['nationality'])){
        
        $nationality = $_POST['nationality'];
        
    }
    
    if(isset($_POST['birth'])){
        
        $birth = $_POST['birth'];
        
    }
    
    if(isset($_POST['phone'])){
        
        $phone = $_POST['phone'];
        
    }
    
    $userObject = new User();
    
    // Registration
    
    if(!empty($username) && !empty($password) && !empty($email) && !empty($first_name) && !empty($last_name) && !empty($identification) && !empty($nationality) && !empty($birth) && !empty($phone)){
        
        $hashed_password = md5($password);
        
        $json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email, $first_name, $last_name, $identification, $nationality, $birth, $phone);
        
        echo json_encode($json_registration);
        
    }
    
    // Login
    
    if(!empty($username) && !empty($password)){
        
        $hashed_password = md5($password);
        
        $json_array = $userObject->loginUsers($username, $hashed_password);
        
        echo json_encode($json_array);
    }
?>