//
//  DebugPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 10/06/2024.
//

import SwiftUI


struct DebugPageView: View {
    private let newsletterService = NewsletterService()
    private let qvstService = QvstService()
    
    @State var newsletterFetch = ""
    @State var qvstFetch = ""
    @State var qvstActiveFetch = ""
    
    var body: some View {
        ScrollView {
            VStack {
                SectionView(
                    title: "NewsletterService : fetchNewsletters",
                    codeBlock: newsletterFetch)
                .onTapGesture {
                    Task  {
                        if let ns = await newsletterService.fetchNewsletters(){
                            newsletterFetch = String(describing: ns)
                        }
                    }
                }
                SectionView(
                    title: "QvstService : fetchAllCampaigns",
                    codeBlock:qvstFetch)
                .onTapGesture {
                    Task  {
                        if let ql = try await qvstService.fetchAllCampaigns()  {
                            qvstFetch = String(describing: ql)
                        }
                    }
                }
                SectionView(
                    title: "QvstService : fetchActiveCampaigns",
                    codeBlock:qvstActiveFetch)
                .onTapGesture {
                    Task  {
                        if let ql = try await qvstService.fetchActiveCampaigns()  {
                            qvstActiveFetch = String(describing: ql)
                        }
                    }
                }
            }
            .padding()
        }
    }
    
    private struct SectionView: View {
        var title: String
        var codeBlock: String
        
        var body: some View {
            HStack{
                Text(title)
                    .font(.subheadline)
                    .italic()
                    .padding()
                Spacer()
            }
            if(codeBlock != "") {
                Text(codeBlock)
                    .padding()
                    .background(.black)
                    .padding(.horizontal)
                    .foregroundStyle(.white)
            }
        }
    }
}

#Preview {
    DebugPageView()
}
