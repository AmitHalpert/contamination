{
  description = "A very minimal flake for building and running contamination";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        packages.contamination = pkgs.callPackage ./nix/default.nix { };
        defaultPackage = self.packages.${system}.contamination;

        apps.contamination = flake-utils.lib.mkApp { drv = self.defaultPackage.${system}; name = "contamination"; };
        defaultApp = self.apps.${system}.contamination;
      }
    );
}
