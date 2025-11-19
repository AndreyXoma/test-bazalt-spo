Name:           file-analyzer
Version:        1.0.0
Release:        1
Summary:        Web service for analyzing damaged files

License:        MIT
URL:            https://github.com/AndreyXoma/test-bazalt-spo
Source0:        %{name}-%{version}.tar

BuildRequires:  maven
BuildRequires:  java-17-openjdk-devel
Requires:       java-17-openjdk

%description
Web service for comparing two directories, finding damaged files,
and displaying differences through REST API and web interface.

%prep
%setup -q

%build
mvn -DskipTests package

%install
# Create app install directory
install -d %{buildroot}/usr/share/%{name}

# Copy jar file (autodetected, adjust if needed)
cp target/*.jar %{buildroot}/usr/share/%{name}/%{name}.jar

# Install python file scrambler
install -D -m 0755 file_scrumbler.py %{buildroot}/usr/bin/file_scrumbler

%files
%doc README.md
/usr/share/%{name}
/usr/bin/file_scrumbler


%changelog
* Wed Nov 18 2025 Andrey Khomyakov <ahomakov89@gmail.com> - 1.0.0-1
- Initial RPM release
