//
//  StudyWideget.swift
//  StudyWideget
//
//  Created by 김호성 on 2024.12.02.
//

import WidgetKit
import SwiftUI

struct Provider: AppIntentTimelineProvider {
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), configuration: ConfigurationAppIntent())
    }

    func snapshot(for configuration: ConfigurationAppIntent, in context: Context) async -> SimpleEntry {
        SimpleEntry(date: Date(), configuration: configuration)
    }
    
    func timeline(for configuration: ConfigurationAppIntent, in context: Context) async -> Timeline<SimpleEntry> {
        var entries: [SimpleEntry] = []

        // Generate a timeline consisting of five entries an hour apart, starting from the current date.
        let currentDate = Date()
        for hourOffset in 0 ..< 5 {
            let entryDate = Calendar.current.date(byAdding: .hour, value: hourOffset, to: currentDate)!
            let entry = SimpleEntry(date: entryDate, configuration: configuration)
            entries.append(entry)
        }

        return Timeline(entries: entries, policy: .atEnd)
    }

//    func relevances() async -> WidgetRelevances<ConfigurationAppIntent> {
//        // Generate a list containing the contexts this widget is relevant in.
//    }
}

struct SimpleEntry: TimelineEntry {
    let date: Date
    let configuration: ConfigurationAppIntent
}

struct StudyWidegetEntryView : View {
    var entry: Provider.Entry
    
    @State private var index: Int? = Calendar.current.dateComponents([.day], from: .distantPast, to: .now).day
    
    @State private var kanjis: [Kanji] = []
    @State private var vocabularies: [Vocabulary] = []
    
    var body: some View {
        
        VStack {
            if let index = index {
                switch entry.configuration.studyPart {
                case .jlptN5, .jlptN4, .jlptN3, .jlptN2, .jlptN1:
                    if vocabularies.count != 0 {
                        let vocabulary: Vocabulary = vocabularies[index % vocabularies.count]
                        Text(vocabulary.sound)
                            .foregroundStyle(Color(UIColor.secondaryLabel))
                            .font(Font.system(size: 14))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                        var nsAttributedString: NSMutableAttributedString {
                            let nsAttributedString = NSMutableAttributedString(string: vocabulary.word)
                            nsAttributedString.addAttribute(.languageIdentifier, value: "ja", range: NSRange(location: 0, length: vocabulary.word.count))
                            return nsAttributedString
                        }
                        Text(AttributedString(nsAttributedString))
                            .font(Font.system(size: 32))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                        Text(vocabulary.meaning)
                            .font(Font.system(size: 18))
                            .lineLimit(2)
                            .minimumScaleFactor(0.5)
                            .multilineTextAlignment(.center)
                    }
                case .kanjiElementary1, .kanjiElementary2, .kanjiElementary3, .kanjiElementary4, .kanjiElementary5, .kanjiElementary6, .kanjiMiddle:
                    if kanjis.count != 0 {
                        let kanji: Kanji = kanjis[index % kanjis.count]
                        Text(kanji.jpSound)
                            .foregroundStyle(Color(UIColor.secondaryLabel))
                            .font(Font.system(size: 14))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                        Text(kanji.jpMeaning)
                            .foregroundStyle(Color(UIColor.secondaryLabel))
                            .font(Font.system(size: 14))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                        var nsAttributedString: NSMutableAttributedString {
                            let nsAttributedString = NSMutableAttributedString(string: kanji.kanji)
                            nsAttributedString.addAttribute(.languageIdentifier, value: "ja", range: NSRange(location: 0, length: kanji.kanji.count))
                            return nsAttributedString
                        }
                        Text(AttributedString(nsAttributedString))
                            .font(Font.system(size: 64))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                            
                        Text(kanji.eumhun)
                            .font(Font.system(size: 18))
                            .lineLimit(1)
                            .minimumScaleFactor(0.5)
                    }
                }
            }
            
//            Text(entry.configuration.studyPart.rawValue)
        }.onAppear(perform: {
            loadData()
        })
    }
    
    private func loadData() {
        let studyPart = entry.configuration.studyPart
        if let jsonData = JSONManager.shared.openJSON(path: studyPart.getFileName()) {
            switch studyPart {
            case .jlptN5, .jlptN4, .jlptN3, .jlptN2, .jlptN1:
                vocabularies = JSONManager.shared.decodeJSONtoVocabularyArray(jsonData: jsonData)
            case .kanjiElementary1, .kanjiElementary2, .kanjiElementary3, .kanjiElementary4, .kanjiElementary5, .kanjiElementary6, .kanjiMiddle:
                kanjis = JSONManager.shared.decodeJSONtoKanjiArray(jsonData: jsonData)
            }
        }
    }
}

struct StudyWideget: Widget {
    let kind: String = "StudyWideget"

    var body: some WidgetConfiguration {
        AppIntentConfiguration(kind: kind, intent: ConfigurationAppIntent.self, provider: Provider()) { entry in
            StudyWidegetEntryView(entry: entry)
                .containerBackground(.fill.tertiary, for: .widget)
        }
    }
}

extension ConfigurationAppIntent {
    fileprivate static var kanjiElementary1: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary1
        return intent
    }
    fileprivate static var kanjiElementary2: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary2
        return intent
    }
    fileprivate static var kanjiElementary3: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary3
        return intent
    }
    fileprivate static var kanjiElementary4: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary4
        return intent
    }
    fileprivate static var kanjiElementary5: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary5
        return intent
    }
    fileprivate static var kanjiElementary6: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiElementary6
        return intent
    }
    fileprivate static var kanjiMiddle1: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .kanjiMiddle
        return intent
    }
    fileprivate static var jlptN5: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .jlptN5
        return intent
    }
    fileprivate static var jlptN4: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .jlptN4
        return intent
    }
    fileprivate static var jlptN3: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .jlptN3
        return intent
    }
    fileprivate static var jlptN2: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .jlptN2
        return intent
    }
    fileprivate static var jlptN1: ConfigurationAppIntent {
        let intent = ConfigurationAppIntent()
        intent.studyPart = .jlptN1
        return intent
    }
}

#Preview(as: .systemSmall) {
    StudyWideget()
} timeline: {
    SimpleEntry(date: .now, configuration: .kanjiElementary1)
    SimpleEntry(date: .now, configuration: .jlptN5)
}
