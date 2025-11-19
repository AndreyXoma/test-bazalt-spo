Name:           file-analyzer
Version:        1.0.0
Release:        1%{?dist}
Summary:        File Analyzer Web Service

License:        MIT
URL:            http://example.com

BuildArch:      noarch

%description
Web service for analyzing damaged files.

%prep
# ничего не делаем, JAR уже готов

%build
# ничего не строим, JAR уже собран

%install
mkdir -p %{buildroot}/opt/file-analyzer
cp %{_sourcedir}/test-bazalt-spo-0.0.1-SNAPSHOT.jar %{buildroot}/opt/file-analyzer/

%files
/opt/file-analyzer/test-bazalt-spo-0.0.1-SNAPSHOT.jar

%changelog
* Wed Nov 18 2025 Andrey Khomyakov <ahomakov89@gmail.com> - 1.0.0-1
- Initial RPM release
