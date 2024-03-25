# Skateboard Company Inventory Tracking GUI
This GUI application began as a simple class project meant to introduce students to the Java language and the JVM as a whole, then expanded from there to become a basic representation of a user interface that employees of a small company could use to keep track of inventory. 

The application itself is meant to be intuitive to use, with little explanation necessary for its operation to be understood. 

# Video Demonstration of the Application

Below are some simple videos demonstrating the basic functionality of the application.  

## Add Part 
<img src="/demo_gifs/add-part.gif" width="650" height="300"/>

## Modify Part 
<img src="/demo_gifs/modify_part.gif" width="650" height="300"/>

## Delete Part 
<img src="/demo_gifs/delete_part.gif" width="650" height="300"/>

## Add Product
<img src="/demo_gifs/add_product.gif" width="650" height="300"/>

## Modify Product
<img src="/demo_gifs/modify_product.gif" width="650" height="300"/>

## Search Items 
<img src="/demo_gifs/search_items.gif" width="650" height="300"/>

# Built Using
• JavaFX
• Maven

# Challenges Overcome & Lessons Learned
• Learning about Inheritance and implementing it was difficult, as there were two different kinds of parts that needed to be represented as their own class from a basic super class.
• At this point in time, this was the largest code base I had ever created or worked with, so staying on top of comments and documentation was necessary both while building the project and so that when I came back to it after significant absence I could pick up where I left off. 
• Packaging the JAR file so that the application would run by itself outside of an IDE was an incredibly frustrating process, but I succeeded in sifting through documentation and command line troubleshooting to properly package everything and run it successfully. 

# Current Known Issues
• There are moments when a Product will not delete correctly
• The application window itself does not resize properly when prompted by the user.

# Planned Updates
• Adding a dedicated backend so that data can persist across machines and application instances
• Improving deletion functionality so that mass deletion of parts or products can be possible
• Creating a login screen so that security is enhanced
