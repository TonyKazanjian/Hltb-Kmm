//
//  SearchViewController.swift
//  KaMPKitiOS
//
//  Created by Anthony Kazanjian on 1/17/22.
//  Copyright © 2022 Touchlab. All rights reserved.
//

import Foundation
import shared
import UIKit

class SearchViewController: UIViewController {
    
    let searchInteractor = IOSSearchInteractor()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        searchInteractor.search(query: "Yakuza", page: 1) {response,error in
            
            print("\(String(describing: response))")
        }
    }
    
}
