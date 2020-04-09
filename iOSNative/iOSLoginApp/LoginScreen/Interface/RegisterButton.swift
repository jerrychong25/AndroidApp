//
//  RegisterButton.swift
//  LoginScreen
//
//  Created by High Sierra on 15/07/2019.
//  Copyright © 2019 iOS App Templates. All rights reserved.
//

import UIKit

let kRegisterButtonBackgroundColor = UIColor(displayP3Red: 31/255, green: 75/255, blue: 164/255, alpha: 1)
let kRegisterButtonTintColor = UIColor.white
let kRegisterButtonCornerRadius: CGFloat = 13.0

class RegisterButton: UIButton {
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.configureUI()
    }
    
    private func configureUI() {
        self.backgroundColor = kRegisterButtonBackgroundColor
        self.layer.cornerRadius = kRegisterButtonCornerRadius
        self.tintColor = kRegisterButtonTintColor
        self.titleLabel?.font = UIFont(name: "AppleSDGothicNeo-Regular", size: 14)
    }
}

