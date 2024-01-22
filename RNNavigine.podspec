require "json"

Pod::Spec.new do |s|
    package = JSON.parse(File.read(File.join(File.dirname(__FILE__), "package.json")))
    s.name         = "RNNavigine"
    s.version      = package["version"]
    s.summary      = package["description"]
    s.homepage     = "navigine.com"
    s.license      = "MIT"
    s.author       = { package["author"]["name"] => package["author"]["email"] }
    s.platform     = :ios, "13.0"
    s.source       = { :git => "https://github.com/navigine/RNNavigine.git", :tag => "master" }
    s.source_files = "ios/**/*.{h,m}"

    s.dependency "React"
    s.dependency "Navigine", "2.4.4"
end
