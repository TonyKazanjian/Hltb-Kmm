//
//  AppDelegate.swift
//  KaMPKitiOS
//
//  Created by Kevin Schildhorn on 12/18/19.
//  Copyright © 2019 Touchlab. All rights reserved.
//

import SwiftUI
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    // Lazy so it doesn't try to initialize before startKoin() is called
    // swiftlint:disable force_cast
    lazy var log = koin.loggerWithTag(tag: "AppDelegate")

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
            startKoin()
            // this line is important
            self.window = UIWindow(frame: UIScreen.main.bounds)

            // In project directory storyboard looks like Main.storyboard,
            // you should use only part before ".storyboard" as its name,
            // so in this example name is "Main".
            let storyboard = UIStoryboard.init(name: "Main", bundle: nil)
            
            // controller identifier sets up in storyboard utilities
            // panel (on the right), it is called 'Storyboard ID'
            let viewController = storyboard.instantiateViewController(withIdentifier: "SearchViewController") as! SearchViewController

            self.window?.rootViewController = viewController
            self.window?.makeKeyAndVisible()
            return true
        }
}
