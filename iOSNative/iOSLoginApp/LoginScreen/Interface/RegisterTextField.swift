//
//  RegisterTextField.swift
//  LoginScreen
//
//  Created by High Sierra on 15/07/2019.
//  Copyright © 2019 iOS App Templates. All rights reserved.
//

import UIKit
import QuartzCore

class RegisterTextField: UITextField {
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.configureUI()
    }
    
    private func configureUI() {
        self.layer.borderColor = UIColor.white.cgColor
        self.layer.borderWidth = 0.5
        self.layer.cornerRadius = 4.0
    }
}
