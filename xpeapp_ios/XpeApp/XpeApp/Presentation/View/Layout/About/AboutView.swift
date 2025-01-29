//
//  AboutView.swift
//  XpeApp
//
//  Created by Théo Lebègue on 28/01/2025.
//
import SwiftUI
import xpeho_ui
struct AboutView: View {
    @Binding var isPresented: Bool
    var body: some View {
        VStack(alignment: .leading) {
            Text("À propos")
                .font(.raleway(.bold, size: 20))
                .foregroundStyle(.black)
                .padding(.leading, 10)
                .padding(.bottom, 4)
            Spacer().frame(height: 10)
            
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    Text("Propriétaire:")
                        .font(.raleway(.semiBold, size: 16))
                        .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                    Copyright()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                HStack {
                    Text("Version:")
                        .font(.raleway(.semiBold, size: 16))
                        .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                    VersionCode()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                Confidentiality()
            }
            .padding(.horizontal, 8)
            .frame(maxWidth: .infinity, alignment: .leading)
            Spacer().frame(height: 20)
            
            ClickyButton(
                label: "Ok",
                verticalPadding: 12,
                onPress: {
                    isPresented = false
                }
            )
            .frame(maxWidth: .infinity)
            .padding(.trailing, 16)
        }
        .padding()
        .frame(maxWidth: 300, alignment: .leading)
        .background(.white)
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .shadow(radius: 10)
    }
}
struct Confidentiality: View {
    var body: some View {
        Button(action: {
            UIApplication.shared.open(URL(string: "https://github.com/XPEHO/XpeApp/blob/main/PRIVACY_POLICY.md")!)
        }) {
            Text("Politiques de confidentialité")
                .font(.raleway(.semiBold, size: 16))
                .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                .underline()
        }
    }
}
struct Copyright: View {
    var body: some View {
        Button(action: {
            UIApplication.shared.open(URL(string: "https://www.xpeho.com")!)
        }) {
            Text("XPEHO")
                .font(.raleway(.semiBold, size: 16))
                .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
                .underline()
        }
    }
}
struct VersionCode: View {
    var body: some View {
        let bundle = Bundle.main
        let version = bundle.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? "Unknown"
        Text("v\(version)")
            .font(.raleway(.semiBold, size: 16))
            .foregroundStyle(XPEHO_THEME.CONTENT_COLOR)
    }
}
