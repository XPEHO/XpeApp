//
//  DebugPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 10/06/2024.
//

import SwiftUI

struct DebugPage: View {
    let firebaseAPI = FirebaseAPI.instance
    let wordpressAPI = WordpressAPI.instance
    
    @State var featureFetch = ""
    @State var newsletterFetch = ""
    @State var qvstFetch = ""
    @State var qvstActiveFetch = ""
    @State var keychainFetch = ""
    
    var body: some View {
        ScrollView {
            VStack {
                SectionView(
                    title: "fetchFeatures",
                    codeBlock: featureFetch)
                .onTapGesture {
                    Task  {
                        if let fetch = await firebaseAPI.fetchAllFeatures(){
                            featureFetch = String(describing: fetch)
                        }
                    }
                }
                SectionView(
                    title: "fetchAllNewsletters",
                    codeBlock: newsletterFetch)
                .onTapGesture {
                    Task  {
                        if let fetch = await firebaseAPI.fetchAllNewsletters(){
                            newsletterFetch = String(describing: fetch)
                        }
                    }
                }
                SectionView(
                    title: "fetchAllCampaigns",
                    codeBlock:qvstFetch)
                .onTapGesture {
                    Task  {
                        if let fetch = await wordpressAPI.fetchAllCampaigns()  {
                            qvstFetch = String(describing: fetch)
                        }
                    }
                }
                SectionView(
                    title: "fetchActiveCampaigns",
                    codeBlock:qvstActiveFetch)
                .onTapGesture {
                    Task  {
                        if let fetch = await wordpressAPI.fetchActiveCampaigns()  {
                            qvstActiveFetch = String(describing: fetch)
                        }
                    }
                }
                SectionView(
                    title: "keychainFetch",
                    codeBlock:keychainFetch)
                .onTapGesture {
                    Task  {
                        if let fetch = KeychainManager.instance.getValue(forKey: "user_id")  {
                            keychainFetch = String(describing: fetch)
                            if let fetch = KeychainManager.instance.getValue(forKey: "user_token")  {
                                keychainFetch += "\n-----\n"
                                keychainFetch += String(describing: fetch)
                            }
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
    DebugPage()
}

