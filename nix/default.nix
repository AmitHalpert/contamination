{ lib
, callPackage
, makeWrapper
, jdk11
}:

let
  buildGradle = callPackage ./gradle-env.nix { };
in

buildGradle rec {
  envSpec = ./gradle-env.json;
  pname = "contamination";
  version = "1.0";

  gradleFlags = [ "desktop:dist" ];

  src = ../.;

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    mkdir -p $out/{lib,bin}
    cp desktop/build/libs/desktop-${version}.jar $out/lib/${pname}.jar


    makeWrapper ${jdk11}/bin/java $out/bin/${pname} \
      --add-flags "-jar $out/lib/${pname}.jar"
  '';

  meta = with lib; {
    description = "A 2D-shooter-platformer video-game";
    homepage = "https://github.com/AmitHalpert/contamination";
    license = licenses.gpl3;
  };
}
