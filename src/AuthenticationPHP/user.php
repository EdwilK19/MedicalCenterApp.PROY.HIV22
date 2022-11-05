<?php

include_once 'db-connect.php';

class User{

  private $db;

  private $db_table = "Usuarios";

  public function __construct() {
    $this->db = new DbConnect();
  }

  public function isLoginExist($username, $password) {

    $query = "SELECT * FROM ".$this->db_table." WHERE (username = '$username' OR email = '$username') AND password = '$password' Limit 1";

    $result = mysqli_query($this->db->getDb(), $query);

    if(mysqli_num_rows($result) > 0) {

      mysqli_close($this->db->getDb());

      return true;

    }

    mysqli_close($this->db->getDb());

    return false;

  }

  public function isUsernameExist($username) {

    $query = "SELECT username FROM ".$this->db_table." WHERE username = '$username'";

    $result = mysqli_query($this->db->getDb(), $query);

    if(mysqli_num_rows($result) > 0) {

      return true;

    }

    return false;

  }

  public function isEmailExist($email) {

    $query = "SELECT email FROM ".$this->db_table." WHERE email = '$email'";

    $result = mysqli_query($this->db->getDb(), $query);

    if(mysqli_num_rows($result) > 0){

      return true;

    }

    return false;

  }

  public function isValidEmail($email) {
    return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
  }

  public function createNewRegisterUser($username, $password, $email, $first_name, $last_name, $identification, $nationality, $birth, $phone) {

    $isUsernameExisting = $this->isUsernameExist($username);

    $isEmailExisting = $this->isEmailExist($email);

    if($isUsernameExisting) {
      $json['success'] = 0;
      $json['message'] = "Error en registro. Nombre de usuario ya existente";
    }

    else {

      if($isEmailExisting){
        $json['success'] = 2;
        $json['message'] = "Error en registro. Correo ya existente";
      }

      else {

        $isValid = $this->isValidEmail($email);

        if($isValid)
        {
          $query = "INSERT INTO ".$this->db_table." (username, password, email, first_name, last_name, identification, nationality, birth, phone) VALUES ('$username', '$password', '$email', '$first_name', '$last_name', '$identification', '$nationality', '$birth', '$phone')";

          $inserted = mysqli_query($this->db->getDb(), $query);

          if($inserted == 1) {

            $json['success'] = 1;
            $json['message'] = "Usuario registrado exitosamente";

          } else{

            $json['success'] = 4;
            $json['message'] = "Error en registro.";

          }

        } else{
          $json['success'] = 3;
          $json['message'] = "Error en registro. Correo no es valido";
        }
      }

    }

    mysqli_close($this->db->getDb());

    return $json;

  }

  public function loginUsers($username, $password) {

    $json = array();

    $canUserLogin = $this->isLoginExist($username, $password);

    if($canUserLogin) {

      $json['success'] = 1;
      $json['message'] = "Sesion iniciada exitosamente";

    } else{
      $json['success'] = 0;
      $json['message'] = "Detalles incorrectos";
    }

    return $json;
  }
}

?>
