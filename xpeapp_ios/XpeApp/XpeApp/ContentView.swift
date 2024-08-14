//
//  ContentView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 05/06/2024.
//

import SwiftUI
import xpeho_ui

enum NavigationItem {
    case home, newsletter, cra, vacation, expenseReport, contacts, qvstTemp, debug
}

struct ContentView: View {
    // Layout and routing management
    @State private var isSidebarVisible: Bool = false
    @State private var selectedView: NavigationItem? = .home
    
    // Data states
    @State var newsletters: [Newsletter] = []
    @State var qvstCampaigns: [QvstCampaign] = []
    @State var activeQvstCampaigns: [QvstCampaign] = []
    
    // Cache usage
    @Environment(\.managedObjectContext) var moc
    @FetchRequest(sortDescriptors: []) var cdNewsletters: FetchedResults<CDNewsletter>
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                VStack {
                    Header(isSidebarVisible: $isSidebarVisible)
                    // Router
                    Group {
                        switch selectedView {
                            case .home:
                                HomePageView(
                                    lastNewsletter: newsletters.first,
                                    activeQvstCampaigns: $activeQvstCampaigns
                                )
                            case .newsletter:
                            NewsletterPageView(newsletters: newsletters)
                            case .cra:
                                Text("CRA page placeholder")
                            case .vacation:
                                Text("Vacation page placeholder")
                            case .expenseReport:
                                Text("Expense Report page placeholder")
                            case .contacts:
                                Text("Contacts page placeholder")
                            case .qvstTemp:
                                QvstCampaignsPageView(campaigns: qvstCampaigns)
                            case .debug:
                                DebugPageView()
                            case .none:
                                HomePageView(
                                    lastNewsletter: newsletters.first,
                                    activeQvstCampaigns: $activeQvstCampaigns
                                )
                        }
                    }
                    .padding(.horizontal, 24)
                    Spacer()
                }
                .background(XPEHO_THEME.BACKGROUND_COLOR.scaledToFill().edgesIgnoringSafeArea(.all))
                .frame(width: geometry.size.width, height: geometry.size.height)
                .disabled(self.isSidebarVisible ? true : false)

                // Make the app content darker and unusable while using sidebar, add also a close effect on click
                if isSidebarVisible {
                    Color.black.opacity(0.3)
                        .edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                            self.isSidebarVisible = false
                        }
                }

                Sidebar(
                    isSidebarVisible: $isSidebarVisible,
                    selectedView: $selectedView,
                    geometry: geometry
                )
            }
        }
        .onAppear {
            if newsletters.isEmpty {
                initNewsletters()
            }
            if qvstCampaigns.isEmpty {
                initQvstCampaigns()
            }
        }
    }
    
    // Data init
    private func initQvstCampaigns() {
        Task {
            // Fetch qvst
            guard let campaigns = await QvstCampaign.fetchActive() else { return }
            // Note: We have to do UI updates on the main thread to prevent crashes
            DispatchQueue.main.async {
                qvstCampaigns = campaigns.sorted(by: {$0.startDate > $1.startDate})
                initActiveQvstCampaigns()
            }
        }
    }
    
    private func initActiveQvstCampaigns() {
        Task {
            // Classify campaigns by year and status to get active campaigns
            guard let activeCampaigns = QvstCampaign.classifyCampaigns(campaigns: qvstCampaigns)["open"] else { return }
            activeQvstCampaigns = activeCampaigns
        }
    }
    
    private func initNewsletters() {
        // Init newsletters using cache as a first step
        newsletters = cdNewsletters.map { cdn in
            return Newsletter(
                id: cdn.id ?? "",
                date:cdn.date ?? Date(),
                pdfUrl: cdn.pdfUrl ?? "",
                publicationDate: cdn.publicationDate ?? Date(),
                summary: cdn.summary ?? "")
        }.sorted(by: {$0.date > $1.date})
        
        
        Task {
            // Fetch newsletters to update as a second step
            guard let newNs = await Newsletter.fetchAll() else {return} // Todo: add error logging
            DispatchQueue.main.async {
                newsletters = newNs.sorted(by: {$0.date > $1.date})
            }
            
            // Update cache with newsletter fetched to make sure to keep last data
            for cdn in cdNewsletters {
                moc.delete(cdn)
            }
            
            for n in newNs {
                let cdn = CDNewsletter(context: moc)
                cdn.id = n.id
                cdn.date = n.date
                cdn.pdfUrl = n.pdfUrl
                cdn.publicationDate = n.publicationDate
                cdn.summary = n.summary
                do {
                    try moc.save()
                } catch {
                    print("Error trying to recreate newsletter cache: \(error)")
                }
            }
        }
    }
}

#Preview {
    ContentView()
}
