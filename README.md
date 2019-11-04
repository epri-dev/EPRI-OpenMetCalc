To build EPRI OpenMetCalc 2.x, the following packages are required;  
	•EPRI OpenMetCalc Source   
	•JDK 1.8  
	•Maven 3.3.9  
	•Eclipse IDE for RCP   


1.Prepare the environment  
a)Setting up JDK 1.8  
	1)JDK 1.8 is used to build the source code. It can be obtained from here.  For a 64-bit windows environment, please download ‘jdk-8u231-windows-x64.exe’ otherwise download ‘jdk-8u231-windows-i586.exe’.   
	2)Double click ‘jdk-8u231-windows-x64.exe’ and follow instructions from the installation wizard in to install JDK. By default, JDK will be installed in ‘C:\Program Files\Java\jdk1.8.0_xxx’.   
	3)After JDK 1.8 has been successfully installed, add the installation path to your windows systems environment. Go to “Control Panel” > “System and Security” > “System” > “Advanced system settings” > “Advanced” > “Environment Variables”. Click on New and Set the variable name to “JAVA_HOME” and variable value to your Java installation directory.   

b)Setting up Maven 3.3.9  
	1)Maven is also a key package for building EPRI OpenMetCalc. To set up Maven 3.3.9, download and extract the files from here. Copy or extract the folder apache-maven-3.3.9-bin to your desired directory.   
	2)Add Maven 3.3.9 to your windows environment. To add maven to your Set up Maven environment go to “Control Panel” > “System and Security” > “System” > “Advanced system settings” > “Advanced” > “Environment Variables”. Click new and first set up “MAVEN_HOME”.  The variable value will be path to which Maven files were extracted.    
	3)Next, add the path to Maven bin to your windows environment. The Maven bin folder is located inside the apache-maven-3.3.9 folder. Set Variable value to “PATH” then set Variable name to the path to the bin folder inside your Maven folder.  

c)Setting up the Eclipse IDE for RCA.  
	1)For developers interested in debugging EPRI OpenMetCalc, the Eclipse IDE for RCA can be used. To set up the Eclipse debugging environment, download the Eclipse IDE files from here.   
	2)Extract the files and navigate to the Eclipse folder.  . /eclipse-rcp-2019-06-R-win32-x86_64>eclipse
	3)Double click “eclipse.exe” to run Eclipse  
	4)After running eclipse.exe workbench will be displayed  
 
d)To explored the EPRI OpenMetCalc source code further, please refer to the Apppenix   


2.Building EPRI OpenMetCalc 2.x  
a)Download or clone EPRI OpenMetCalc source code the EPRI’s GitHub using this link. Extract the source code form the zip files and save it to a desired location.  
b)Open windows command prompt. Go to Start>Type cmd in the search box > select command prompt.     
c)Navigate to the location into which EPRI OpenMetCalc has been extracted in command prompt using cd.  
d)Go to the com.epri folder using cd .\OpenMetCalc\com.epri  
e)To build EPRI OpenMetCalc run the following command “mvn clean verify”. This command automatically downloads all necessary libraries and attempts to build the application.   
f)After a successful build, the output will display on command prompt. The output binary will be stored in a zipped folder at the following location.  
.\OpenMetCalc\com.epri\releng\com.epri.metric_calculator.product\target\products\ EPRI OpenMetCalc-win32.win32.x86.zip  
g)Copy the “EPRI OpenMetCalc-win32.win32.x86.zip” to any designated location on your PC.   
h)Extract files from “EPRI OpenMetCalc-win32.win32.x86.zip” to obtain “EPRI OpenMetCalc-win32.win32.x86”  
i)Navigate to the folder “EPRI OpenMetCalc-win32.win32.x86” and run the executable file named “OpenMetCalc.exe”  
j)To complete installation, follow instructions 1 to 8 from using EPRI OpenMetCalc.exe installer.  



Appendix   
DEBUGING AND TROUBLESHOOTING   

Setting Eclipse IDE Debugging Environment  

To use the Eclipse IDE for debugging, follow the steps below:  
1.To import projects into Eclipse, navigate to [File] > [Import] > [General] > [Existing Projects into Workspace]  
2.Select the root directory-typically where your EPRI OpenMetcalc files are stored.  
3.Check “'Search for nested project” and “Copy projects into workspace” to enable them  
4.Choose deselect all files and review the list of files. The Select the following 5 items.  
	•com.epri   
	•com.epri.metric_calculator   
	•com.epri.metric_calculator.configuration   
	•com.epri.metric_calculator.feature   
	•com.epri.metric_calculator.product  
5.Click ‘Finish’ and the projects will be imported into your Eclipse workspace  


Debugging with Eclipse IDE  

To debug with Eclipse IDE, follow the following instructions:  
1.Navigate to the 'com.epri.metric_calculator.product' application in the project explorer on Eclipse IDE workspace.   
2.Open 'com.epri.metric_calculator.product'   
3.Click the “Overview” tab towards the bottom.   
4.Click “Launch an Eclipse application in Debug mode”   
5.Click “Continue”.  


Troubleshooting Common Debugging Issues  

This section addresses some of the issues commonly faced while debugging EPRI OpenMetCalc using Eclipse IDE. Two cases are specifically considered.  

Case 1:  Debug failure
This will occur when any of the required packages is missing or not properly installed. If message 'mvn is not recognized as an internal or external command...' is shown, when mvn build is attempted, please follow the procedure below to setup the environment variables for Maven  
Open `Control Panel` > `System and Security` > `System` > `Advanced system settings` > ‘Advanced’ tab > `Environment Variables…` and add the path of Maven’s ‘bin’ path into the ‘Path’s variable in ‘System variables’  

Case 2: The dialog for Launch Error is shown  
A launch error would occur when Eclipse displays errors indicating that the application cannot be run. The most common cause of this the absence of packages required to build the application.     
1.If dialog for 'Launch Error' is shown, please close it.   
2.Go to ‘Run’ > ‘Debug Configurations’    
3.Click on the ‘Plug-ins’ tab Click the ‘Add Required Plug-ins’   
4.Click ‘Debug’  

Case 3: Splash is not shown  
If the 'Splash Screen” is does not display when OpenMetCalc is launched, please follow the procedure below to address the issue.   
1.Navigate to 'com.epri.metric_calculator' in Eclipse project explorer.   
2.Open the 'plugin.xml' file to edit it   
3.Click the ‘Extensions’ tab.   
4.Right-click ‘org.eclipse.ui.splashHandlers’ select ‘New’ > ‘splashHandlerProductBinding’  
5.Enter the following splashId and productId:  
	•splashId: com.epri.metric_calculator.splashHandlers.interactive    
	•productId: com.epri.metric_calculator.product  

