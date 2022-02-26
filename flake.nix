{
  description = "A very minimal flake for building and running contamination";

  nixConfig.extra-substituters = [ "https://contamination.cachix.org" ];
  nixConfig.extra-trusted-public-keys = [ "contamination.cachix.org-1:KmdW5xVF8ccKEb9tvK6qtEMW+lGa83seGgFyBOkeM/4=" ];

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    nix-filter.url = "github:numtide/nix-filter";
  };

  outputs = { self, nixpkgs, flake-utils, nix-filter }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        packages.contamination = pkgs.callPackage ./nix/default.nix { nix-filter = nix-filter.lib; };
        defaultPackage = self.packages.${system}.contamination;

        apps.contamination = flake-utils.lib.mkApp { drv = self.defaultPackage.${system}; name = "contamination"; };
        defaultApp = self.apps.${system}.contamination;
      }
    );
}
