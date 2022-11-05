<?php
    
    require_once 'user.php';
    
    $inputJSON = file_get_contents('php://input');
    $input = json_decode($inputJSON, TRUE);
    
    
    $userObject = new User();
    
    // Login
    
    if(isset($input['username']) && isset($input['password'])){
        
        $username = $input['username'];
        $password = $input['password'];
        
        $hashed_password = md5($password);
        
        $json_array = $userObject->loginUsers($username, $hashed_password);
        
        echo json_encode($json_array);
    }
?>