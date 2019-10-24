"# EPRI-OpenMetCalc" 
"# EPRI-OpenMetCalc" 

Build Instructions for Windows
Prerequisites
•	JDK 1.8
•	Maven 3.3.9
•	Eclipse IDE for RCP
•	OpenMetCalc Source Code(OpenMetCalc.zip) (To be cloned or downlaoded from Gitub)
Prepare Environment 
•	JDK 1.8 is used for Build (It is necessary to build the source code)
1.	In the cases of Windows 64 bits Environment, please download ‘jdk-8u231-windows-x64.exe’
2.	Double click ‘jdk-8u231-windows-x64.exe’ to install JDK, and it is installed, as a default, in the directory ‘C:\Program Files\Java\jdk1.8.0_xxx’
3.	Add the environment variables of ‘JAVA_HOME’ into the system environment as like below procedure: Open `Control Panel` > `System and Security` > `System` > `Advanced system settings` > ‘Advanced’ tab > `Environment Variables…` and click ‘New’ to add the path of JDK’s installation path in ‘System variables’

•	Maven 3.3.9 is used for Build (It is necessary to build the source code)
•	Navigat to  and download
•	Extract  copy and 
4.	Extract the Maven 3.3.9 (apache-maven-3.3.9-bin.zip) 
5.	Add the environment variables of Maven into the system environment as like below procedure: Open `Control Panel` > `System and Security` > `System` > `Advanced system settings` > ‘Advanced’ tab > `Environment Variables…` and add the path of Maven’s ‘bin’ path into the ‘Path’s variable in ‘System variables’
•	Eclipse IDE for RCP is used for Debug (if Debug is not necessary, it does not need to be prepared)
1.	Extract the Eclipse IDE for RCP (eclipse-rcp-2019-06-R-win32-x86_64.zip) 
2.	Double click ‘eclipse.exe’ to run Eclipse
3.	Refer to “Debug Environment with Eclipse IDE”
How to Build
1.	Extract the source code of OpenMetCalc from the ‘OpenMetCalc.zip’ 
2.	Open Command Prompt in the folder that is extracted
3.	In Command Prompt, navigate to the ‘com.epri’ of the OpenMetCalc directory 
 
4.	Enter the following command to build the application
 
It will download all necessary library automatically then try to build by itself
5.	Once the build is completed, the built binary as zipped file will be output as like below:
 
6.	Copy the file, ‘EPRI OpenMetCalc-win32.win32.x86.zip’, to any designated location on your PC.
7.	Extract the file, ‘EPRI OpenMetCalc-win32.win32.x86.zip’ to get ‘EPRI OpenMetCalc-win32.win32.x86’
8.	Navigate into ‘EPRI OpenMetCalc-win32.win32.x86’ 
9.	The executable file, named ‘OpenMetCalc.exe’ can be found
10.	Follow the instructions from installing from Binary


Debug Environment with Eclipse IDE
•	Import projects in Eclipse
1.	Navigate into [File] > [Import] > [General] > [Existing Projects into Workspace]
2.	Select the root directory-typically where your EPRI OpenMetcalc files are 
Deselect, 
3.	Check on 'Search for nested project' to enable
4.	Check on 'Copy projects into workspace' to enable
5.	Select following 5 items
•	com.epri
•	com.epri.metric_calculator
•	com.epri.metric_calculator.configuration
•	com.epri.metric_calculator.feature
•	com.epri.metric_calculator.product
6.	Click on ‘Finish’ and the projects will be imported in your workspace


Debug with Eclipse IDE
1.	Navigate into 'com.epri.metric_calculator.product' in the project explorer
2.	Open the 'com.epri.metric_calculator.product'
3.	Click [Overview] tab
4.	Click on [Launch an Eclipse application in Debug mode]
5.	Click on [Continue] 
 
Troubleshooting
Case: debug is failed
If message 'mvn is not recognized as an internal or external command...' is shown, when mvn build is attempted, please follows below procedure to setup the environment variables for Maven
Open `Control Panel` > `System and Security` > `System` > `Advanced system settings` > ‘Advanced’ tab > `Environment Variables…` and add the path of Maven’s ‘bin’ path into the ‘Path’s variable in ‘System variables’
Case: The dialog for Launch Error is shown
1.	If dialog for 'Launch Error' is shown, please close it 
2.	Go to the [Run] > [Debug Configurations] 
3.	Click the [Plug-ins] tab
4.	Click the [Add Required Plug-ins]
5.	Click the [Debug]
Case: Splash is not shown
If 'Splash Screen” is now shown, when OpenMetCalc is launched, please follows below procedure to resolve
1.	Navigate into 'com.epri.metric_calculator' in the project explorer
2.	Open the 'plugin.xml' to edit
3.	Click the [Extensions] tab
4.	Right-click on the [org.eclipse.ui.splashHandlers]
5.	Click the [New] > [splashHandlerProductBinding]
6.	Enter the splashId and productID, as like below
•	splashId: com.epri.metric_calculator.splashHandlers.interactive
•	productId: com.epri.metric_calculator.product
