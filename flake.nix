{
  description = "A very minimal flake for building and running contamination";

  nixConfig.extra-substituters = [ "https://contamination.cachix.org" ];
  nixConfig.extra-trusted-public-keys = [ "contamination.cachix.org-1:KmdW5xVF8ccKEb9tvK6qtEMW+lGa83seGgFyBOkeM/4=" ];

  inputs.nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
  inputs.nix-filter.url = "github:numtide/nix-filter";

  outputs = { self, nixpkgs, nix-filter }:
    let
      supportedSystems = [ "x86_64-linux" "x86_64-darwin" "aarch64-linux" ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
      nixpkgsFor = forAllSystems (system: nixpkgs.legacyPackages.${system});
    in
    {
      packages = forAllSystems (system: { contamination = nixpkgsFor.${system}.callPackage ./nix/default.nix { nix-filter = nix-filter.lib; inherit self; }; });
      defaultPackage = forAllSystems (system: self.packages.${system}.contamination);

      apps = forAllSystems (system: { contamination = { type = "app"; program = "${self.packages.${system}.contamination}/bin/contamination"; }; });
      defaultApp = forAllSystems (system: self.apps.${system}.contamination);
    };
}
