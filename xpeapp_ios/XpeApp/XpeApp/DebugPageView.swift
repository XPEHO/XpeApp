//
//  DebugPageView.swift
//  XpeApp
//
//  Created by Loucas Cubeddu on 10/06/2024.
//

import SwiftUI


struct DebugPageView: View {
    @State var newsletterFetch = ""
    @State var qvstListFetch = ""
    
    var body: some View {
        ScrollView {
            VStack {
                SectionView(
                    title: "Newsletter fetch",
                    codeBlock: newsletterFetch)
                .onTapGesture {
                    Task  {
                        if let ns = await Newsletter.fetchAll(){
                            DispatchQueue.main.async {
                                newsletterFetch = String(describing: ns)
                            }
                        }
                    }
                }
                SectionView(
                    title: "QVST List fetch",
                    codeBlock:qvstListFetch)
                .onTapGesture {
                    Task  {
                        if let ql = await QvstCampaign.fetchActive()  {
                            qvstListFetch = String(describing: ql)
                        }
                    }
                }
                Spacer()
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
    
    private func fetchQvstList() async {
        let urlStr = "http://yaki.uat.xpeho.fr:7830/wp-json/xpeho/v1/qvst/campaigns:active"
        guard let url = URL(string: urlStr) else {
            print("Malformed url \(urlStr)")
            return
        }
        do {
            let data = try await URLSession.shared.data(from: url).0
            do {
                guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [[String: Any]] else {
                    print("Error parsing json")
                    return
                }
                
                DispatchQueue.main.async {
                    qvstListFetch = String(describing: json)
                    print("fetch: \(qvstListFetch)")
                }
            } catch {
                print("Error parsing json: \(error)")
            }
        } catch {
            print("Error trying to retrieve list of qvst: \(error)")
            return
        }
    }

}

#Preview {
    DebugPageView()
}
