<?php
    
    require_once 'user.php';
    
    $inputJSON = file_get_contents('php://input');
    $input = json_decode($inputJSON, TRUE);
    
    $userObject = new User();
    
    // Registration
    
    if(isset($input['username']) && isset($input['password']) && isset($input['email']) && isset($input['first_name']) && isset($input['last_name']) && isset($input['identification']) && isset($input['nationality']) && isset($input['birth']) && isset($input['phone'])){
        
        $username = $input['username'];
        $password = $input['password'];
        $email = $input['email'];
        $first_name = $input['first_name'];
        $last_name = $input['last_name'];
        $identification = $input['identification'];
        $nationality = $input['nationality'];
        $birth = $input['birth'];
        $phone = $input['phone'];
        
        $hashed_password = md5($password);
        
        $json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email, $first_name, $last_name, $identification, $nationality, $birth, $phone);
        
        echo json_encode($json_registration);
        
    }
    
?>