import 'screens/home_screen.dart';
import 'package:flutter/material.dart';


void main()
{
  runApp(DietPlannerApp());
}
class DietPlannerApp extends StatelessWidget {
  const DietPlannerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,//debug mode nhi dikhega
      title: "Diet Planner",
      theme: ThemeData(
          primarySwatch: Colors.blue
      ),
      home: HomeScreen(),
    );
  }
}
