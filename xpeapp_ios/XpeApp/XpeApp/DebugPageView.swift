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
}

#Preview {
    DebugPageView()
}
