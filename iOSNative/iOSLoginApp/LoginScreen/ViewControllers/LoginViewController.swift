//
//  LoginViewController.swift
//  LoginScreen
//
//  Created by Florian Marcu on 1/15/17.
//  Copyright © 2017 iOS App Templates. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {

    @IBOutlet var emailTextField: UITextField!
    @IBOutlet var passwordTextField: UITextField!
    @IBOutlet var forgotpasswordLabel: UILabel!

    override func viewDidLoad() {
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(LoginViewController.tapFunction))
        forgotpasswordLabel.isUserInteractionEnabled = true
        forgotpasswordLabel.addGestureRecognizer(tap)
    }
    
    @IBAction func didTapLoginButton(_ sender: LoginButton) {
        NSLog("LoginViewController didTapLoginButton()")
        
        // Regular login attempt. Add the code to handle the login by email and password.
        guard let email = emailTextField.text, let password = passwordTextField.text else {
            // It should never get here
            return
        }
        
        NSLog("LoginViewController email: " + email)
        NSLog("LoginViewController password: " + password)
        
        didLogin(method: "email and password", info: "\nEmail: \(email) \n Password: \(password)")
    }
    
    @IBAction func didTapRegisterButton(_ sender: RegisterButton) {
        NSLog("LoginViewController didTapRegisterButton()")
        
        // Regular register attempt. Add the code to handle the register by full name, email and password.
        didRegister()
    }

    @IBAction func tapFunction(_ sender: UITapGestureRecognizer) {
        NSLog("LoginViewController Label Clicked!")
        
        // Switch To Register Page
        performSegue(withIdentifier: "register", sender: self)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        NSLog("LoginViewController touchesBegan()")

        emailTextField.resignFirstResponder()
        passwordTextField.resignFirstResponder()
    }

    private func didLogin(method: String, info: String) {
        NSLog("LoginViewController didLogin()")
        
//        let message = "Successfully logged in with \(method). \n" + info
        let message = info
        let alert = UIAlertController(title: "Success", message: message, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Done", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
        
//        // Switch To Web Page
//        performSegue(withIdentifier: "webView", sender: self)
    }
    
    private func didRegister() {
        NSLog("LoginViewController didRegister()")
        
        // Switch To Register Page
        performSegue(withIdentifier: "register", sender: self)
    }
}
