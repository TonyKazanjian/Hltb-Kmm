//
//  IOSSearchInteractor.swift
//  KaMPKitiOS
//
//  Created by Anthony Kazanjian on 1/14/22.
//  Copyright © 2022 Touchlab. All rights reserved.
//

import Foundation
import shared
import SwiftSoup

class IOSSearchInteractor: SearchInteractor {
    func search(query: String, page: Int32, completionHandler: @escaping (HowLongToBeatResponse?, Error?) -> Void) {
        searchGames(query: query) { (result) -> Void in
            switch(result) {
            case let .success(htmlString):
                completionHandler( try? self.scrapeGameSearch(htmlAsString: htmlString), nil)
            case let .failure(error):
                print("Error fetching image for photo: \(error)")
            }
        }
    }
    private func searchGames(query: String, completion: @escaping (Result<String, Error>) -> Void) {

        let hltbUrl = URL(string: "https://howlongtobeat.com/search_results?page=1")
        var request = URLRequest(url: hltbUrl!)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-type")
        let body: [String: String] = [
            "queryString": query,
            "t": "games",
            "sorthead": "popular",
            "sortd": "0",
            "plat": "",
            "length_type": "main",
            "length_min": "",
            "length_max": "",
            "detail": "0"
        ]
        var data = [String]()
        for (key, value) in body {
            data.append(key + "=\(value)")
        }
        let formString: String = data.map { String($0) }.joined(separator: "&")
        request.httpBody = formString.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                 print("Post Request Error: \(error.localizedDescription)")
                 return
               }

               // ensure there is valid response code returned from this HTTP response
               guard let httpResponse = response as? HTTPURLResponse,
                     (200...299).contains(httpResponse.statusCode)
               else {
                 print("Invalid Response received from the server")
                 return
               }
            let str = String(decoding: data!, as: UTF8.self)
            completion(.success(str))

        }
        task.resume()
    }
    private func scrapeGameSearch(htmlAsString: String) throws -> HowLongToBeatResponse {
        let document = try SwiftSoup.parse(htmlAsString).body()
        var entryArray: [HowLongToBeatEntry] = []
        let entryBuilder = HowLongToBeatEntry.Builder
            .init(title: "", id: 0, imageUrl: "", timeLabels: [String: String]())
        // get # of results
    //    let div = try document?.getElementsByClass("global_padding shadow_box back_blue center")
    //    print(try div?.first()?.text())
        // searches through all li elements
        for (liElement) in try document!.getElementsByClass("back_darkish") {
            // gets title and jpeg
            let gameLabel = try liElement.getElementsByTag("a")
            let title = try gameLabel.text()
            let boxArt = try gameLabel.first()?.getElementsByTag("img").first()?.attr("src")
            entryBuilder
                .setTitle(title: title)
                .setImageUrl(imageUrl: boxArt!)
            let timeDetails = try liElement.getElementsByClass("search_list_details_block").first()?.children().first()
            let storyKey = "shadow_text"
            let timeKey = "center time"
            var storyChunk = ""
            var timeLength = ""
            var timeDict = [String: String]()
            try timeDetails?.getAllElements().forEach { chunk in
                if (try chunk.attr("class").contains(storyKey)) {
                    storyChunk = try chunk.text()
                }
                if (try chunk.attr("class").contains(timeKey)) {
                    timeLength = try chunk.text()
                }
                timeDict[storyChunk] = timeLength
            }
            entryBuilder.setTimeLabels(timeLabels: timeDict)
            entryArray.append(entryBuilder.build())
        }
        return HowLongToBeatResponse.init(statusCode: 200, entryList: entryArray, totalResults: 0)
    }   
}
