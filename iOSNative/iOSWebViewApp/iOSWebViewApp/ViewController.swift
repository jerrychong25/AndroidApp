//
//  ViewController.swift
//  iOSWebViewTutorial
//
//  Created by High Sierra on 17/04/2019.
//  Copyright © 2019 Explosoft. All rights reserved.
//

import UIKit
import WebKit

class ViewController: UIViewController {

    var webView: WKWebView!

    override func loadView() {
        webView = WKWebView(frame: .zero, configuration: WKWebViewConfiguration())
        self.view = webView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        webView.load(URLRequest(url: URL(string: "http://www.apple.com/")!))
    }
}

