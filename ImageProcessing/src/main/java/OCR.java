/*
 * Author's name and email: Michael, michaeljava95@gmail.com
 * Program description: This class is used by Menu class to OCR images.
 * Latest version: 5:49 PM, 5/3/2019. Now the program has multiple classes.
 * Older versions: 2:44 PM, 4/25/2019.
 *	 Didnt record older versions.
 */

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;
import javax.swing.JOptionPane;

final public class OCR
{
	int studentNumber;
	/* securityflag is used in this class only. It is used to see if the card we 
	 read includes at least one of the words MUHENDISLIK, FAK, BILGISAYAR or MUH.
	 We do this in order to prevent students from writing their student number on 
	 a paper and scan it to the program. Students wont know about this secret 
	 security step of the program. */
	boolean securityFlag = false;
	/* We use set and reset security flag in this class but we use it in class 
	 Menu. We use this flag when submit button is clicked in order to see if 
	 current studentNumber variable of ocr object is a valid one. (Starts with 13, 
	 10 characters long, securityFlag is true.
	  If manualSubmit button is used instead of submit button, these checks are 
	 done in method manualSubmitActionPerformed */
	boolean securityFlag2 = false; // Used to 
	/* This variable is used to determine whether we will create a new record or 
	 update an existing record. */
	String operation;
	Database db = new Database();

	public String analyze(String className)
	{
		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();
		// Initialize tesseract-ocr with English, without specifying tessdata path
		if (api.Init(null, "eng") != 0)
		{
			System.err.println("Could not initialize tesseract.");
			System.exit(1);
		}

		// Open input image with leptonica library
		PIX image = pixRead("D:\\Computer Engineering\\z- Other\\School, Internships"
			+ "\\4. Year\\Term 2\\Finishing Project 2\\mavenproject1\\id.jpg");
		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		//System.out.println("OCR output:\n" + outText.getString());

		// Destroy used object and release memory
		api.End();
		outText.deallocate();
		pixDestroy(image);

		// Splitting text to String array elements.
		String[] studentCardData = outText.getString().split(" ");

		// Parsing String array elements
		for (String data : studentCardData)
		{
			try
			{
				/* I am not checking to see if the string is 10 characters long. 
				 Because student number is read from image with some extra 
				 characters attached to it. So it might be longer. */
				if (data.length() < 10)
				{
					continue;
				}

				// Secret security code.
				if (data.contains("MUHENDISLIK") || data.contains("BILGISAYAR"))
				{
					securityFlag = true;
					continue;
				}

				// We already did length test above.
				if (securityFlag == true)
				{
					String subString1 = data.substring(0, 2);
					String subString2 = data.substring(0, 3);
					
					if ("13".equals(subString1))
					{
						String subString3 = data.substring(0, 10);
						
						// Valid student id and registered.
						if ( db.isRegistered(Integer.parseInt(subString3), className) )
						{
							operation = "update";
							
							securityFlag2 = true; // Valid student number.
							
							// Getting rid of extra characters.
							studentNumber = Integer.parseInt(subString3);
							
							securityFlag = false; // Resetting object to be used again
							return subString3;
						}
						else // Valid student id but not registered.
						{
							String answer = JOptionPane.showInputDialog("StudentID " + 
								subString3 + " is not in student list. Enter \"y\" to add"
								+ " it to student list. Enter \"n\" to take another photo.");
							if ("y".equals(answer))
							{
								operation = "insert";
								
								securityFlag2 = true; // Valid student number.
								
								// Getting rid of extra characters.
								studentNumber = Integer.parseInt(subString3);
								
								securityFlag = false; // Resetting object to be used again
								return subString3;
							}
							else // If user enters something other than "y".
							{
								break;
							}
						}
					}
					else if (":13".equals(subString2) || "113".equals(subString2))
					{
						String subString4 = data.substring(1, 11);
						
						// Valid student id and registered.
						if ( !db.isRegistered(Integer.parseInt(subString4), className) )
						{
							operation = "update";
							
							securityFlag2 = true; // Valid student number.
							
							// Getting rid of extra characters.
							studentNumber = Integer.parseInt(subString4);
							
							securityFlag = false; // Resetting object to be used again
							return subString4;	
						}
						else // Valid student id but not registered.
						{
							String answer = JOptionPane.showInputDialog("StudentID " + 
								subString4 + " is not in student list. Enter \"y\" to add it "
								+ "to student list. Enter \"n\" to take another photo.");
							if ("y".equals(answer))
							{
								operation = "insert";
								
								securityFlag2 = true; // Valid student number.
								
								// Getting rid of extra characters.
								studentNumber = Integer.parseInt(subString4);
								
								securityFlag = false; // Resetting object to be used again
								return subString4;
							}
							else // If user enters something other than "y".
							{
								break;
							}
						}
					}
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				securityFlag = false; // Resetting object to be used again
				securityFlag2 = false; // Invalid student number.
				return "Try again. Make sure your student ID card is about 10 centimeters "
					+ "from the camera.";
			}
		}

		securityFlag = false; // Resetting object to be used again
		securityFlag2 = false; // Invalid student number.
		return "Try again. Make sure your student ID card is about 10 centimeters "
			+ "from the camera.";
	}
}
