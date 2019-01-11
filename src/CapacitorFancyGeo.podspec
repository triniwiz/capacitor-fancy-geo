
  Pod::Spec.new do |s|
    s.name = 'CapacitorFancyGeo'
    s.version = '0.0.2'
    s.summary = 'Geolocation plugin'
    s.license = 'MIT'
    s.homepage = 'https://github.com/triniwiz/capacitor-fancy-geo'
    s.author = 'Osei Fortune'
    s.source = { :git => 'https://github.com/triniwiz/capacitor-fancy-geo', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.dependency 'FancyGeo', '~> 0.0.2'
  end
