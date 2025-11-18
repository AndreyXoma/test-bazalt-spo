Name:           file-analyzer
Version:        1.0.0
Release:        1%{?dist}
Summary:        File Analyzer Web Service

License:        MIT
URL:            https://github.com/AndreyXoma/test-bazalt-spo.git
BuildRequires:  java-17-openjdk maven
Requires:       java-17-openjdk

%description
Web service for analyzing corrupted files between two directories.

%prep
# No source preparation needed

%build
# No build, jar already built via Maven

%install
mkdir -p %{buildroot}/opt/%{name}
cp /src/target/test-bazalt-spo-0.0.1-SNAPSHOT.jar %{buildroot}/opt/%{name}/

%files
/opt/%{name}/test-bazalt-spo-0.0.1-SNAPSHOT.jar

%changelog
* Tue Nov 18 2025 Андрей Хомяков <ahomakov89@gmail.com> - 1.0.0-1
- Initial RPM package with pre-built jar