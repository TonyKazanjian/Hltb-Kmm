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

    @IBOutlet var gameTableView: UITableView!

    let searchViewModel = SearchViewModel(repository: SearchRepository(searchInteractor: IOSSearchInteractor())) { searchState in
//        print("\(searchState)")
        let entries = searchState.entries
        print("\(entries)")
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        searchViewModel.searchGamesByQuery(query: "Yakuza")
    }
}
