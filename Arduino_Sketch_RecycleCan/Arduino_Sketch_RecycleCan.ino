#include <SoftwareSerial.h> 
#include <Servo.h>
SoftwareSerial btm(2,3); // rx tx 
Servo myservo;
int angle = 0; 
int index = 0; 
char data[10]; 
char c; 
boolean flag = false;
void setup() { 
 //pinMode(LED_BUILTIN, OUTPUT);
 myservo.attach(7);  // attaches the servo on pin 9 to the servo object
 myservo.write(0);
 Serial.begin(38400); 
 Serial.println("ready");
 btm.begin(38400); 
 pinMode(8, OUTPUT);
 pinMode(9, OUTPUT);
 pinMode(10, OUTPUT);
} 
void loop() { 
   if(btm.available() > 0){ 
     while(btm.available() > 0){ 
          c = btm.read(); 
          delay(10); //Delay required 
          data[index] = c; 
          Serial.println(data[index]);
          btm.println(data[index]);
          index++; 
     } 
     data[index] = '\0'; 
     flag = true;   
   }  
   if(flag){ 
     processCommand(); 
     flag = false; 
     index = 0; 
     data[0] = '\0'; 
   } 
} 


void processCommand(){ 
 char command = data[0]; 
 char inst = data[1]; 
 switch(command){ 
   case 'A':
      //digitalWrite(LED_BUILTIN, HIGH); 
      digitalWrite(8,HIGH);
      openCan();
      digitalWrite(8,LOW);
      //digitalWrite(LED_BUILTIN, LOW); 
//      btm.println("A recived"); was used for testing
//      Serial.println("A");
   break; 
   case 'B': 
      digitalWrite(9,HIGH);
      openCan();
      digitalWrite(9,LOW);
//      btm.println("B recived"); was used for testing
//      Serial.println("B");
         
   break; 
      case 'C': 
      digitalWrite(10,HIGH);
      openCan();
      digitalWrite(10,LOW);
//      btm.println("C recived"); was used for testing
//      Serial.println("C");
         
//   break;        Only 3 lids for now
//      case 'D': 
//      //digitalWrite(LED_BUILTIN, HIGH); 
//      openCan();
//      //digitalWrite(LED_BUILTIN, LOW);
//      btm.println("D recived"); 
//      Serial.println("D");

         
   break; 
      default:
      openCan();
//      btm.println("Didnt reconize the signal");  was used for testing
//      Serial.println("Def"); 
    break;  
 } 
} 

void openCan()
{
    for(angle=0;angle<120;angle++)
  {
    myservo.write(angle);
    delay(15);
    Serial.println(angle);
  }
  delay(10000);
    for(angle=120;angle>0;angle--)
  {
    myservo.write(angle);
    delay(15);
    Serial.println(angle);
  }  
  
}
