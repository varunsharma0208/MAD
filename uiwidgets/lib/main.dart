//create UI widgets
//import the material design library
import 'package:flutter/material.dart';

// Entry point of the application
void main() {
  runApp(const MyApp());
}

// Main widget
class MyApp extends StatelessWidget {
  const MyApp({super.key});

/*@override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        body: SafeArea//top area of our screen leaving the notification area
        (
          child: Align(
            alignment: Alignment.topCenter,
            child: Padding(
              padding: const EdgeInsets.only(top: 20),
              child: ElevatedButton(
                onPressed: () {
                  print("Login button clicked");
                },
                child: const Text("Login"),
              ),
            ),
          ),
        ),
      ),
    );
  }*/

// to create input text field for "enter username"
/*@override
  Widget build(BuildContext context) {
    // TODO: implement build
  return MaterialApp(
    home: Scaffold(
      body: Padding(
          padding: const EdgeInsets.all(20),
          child: TextField(
            decoration: InputDecoration(
              labelText: "enter your name",
              border: OutlineInputBorder(),
            ),
          ),
      ),
    ),
  );
}
}*/
  /*@override
  Widget build (BuildContext){
    return MaterialApp(
      home: Scaffold(
        body: const Center(
          child: Text(
            "This is my first Flutter application",
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }*/

//create list in flutter
@override
  Widget build(BuildContext context) {
    // TODO: implement build
   return MaterialApp(
     home: Scaffold(
       body : ListView(
         children: const [
           ListTile(
             title:Text('Saumya')),
         ListTile(
           title: Text('Veer Saxena')),
           ListTile(
               title: Text('Virat Kohli')),
           ListTile(
               title: Text('Rohit Sharma')),
         ],
       )
     ),
   );
  }
}

