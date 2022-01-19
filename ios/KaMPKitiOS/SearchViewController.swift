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

class SearchViewController: UITableViewController {

    @IBOutlet var gameTableView: UITableView!

    let searchViewModel = IOSSearchViewModel(repository: SearchRepository(searchInteractor: IOSSearchInteractor())) { searchState in
//        print("\(searchState)")
        let entries = searchState.entries
        print("\(entries)")
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        searchViewModel.setQuery(query: "Yakuza")
    }
}
