////////////////////////////////////////////////////////////////////////
//
// Title:                findingNemo.cpp // lol
// Problem:        finds fish info from data.txt file
// Class:                  ENGR 697
// Date:                  2015
// Author:            Anish Kumaramagalam
//
///////////////////////////////////////////////////////////////////////

/*
data file requirements:

1) all tokens must be separated by a space
2) the last token on the data type column must have a ' ' at the end
3) no single strings with (space)'s ' ' should use (underscore) '_'

*/

#include<iostream>
#include<math.h>
#include<stdlib.h>
#include<fstream>
#include<string.h>
#include<string>
#include<cstring>

using namespace std;

string word(string name)   {
    int i = 0;
    string temp = name;
    name.clear();
    name = temp;

    while (1) {
        if(name[i] == '\0') {
            return name;
        } else  if (name[i] == '_') {
            name[i] = ' ';
        }
        i++;
    }
    
    temp.clear();
    return name;
}

/*int sNum(string num, int number)    { // static non static term
    int lNum = num.length();
    if (num[0] == 's')  {
        return atoi(num.substr(1,lNum-1).c_str());
    }
    
    return atoi(num.c_str()) * number;
}*/

int comp(string s1, string s2) { // compares two strings
    int i = 0, s1l = s1.length(), s2l = s2.length();
    
    if (s1l == s2l)   {
        
        for (; i<s1l; i++) {
            if (s1[i] != s2[i]) {
                return 0;
            }
        }
        return 1;
    }
    return 0;
}

/*  find() finds key that matches with the query */
string find(string query, string key)   {

    string nn[] = {"","","","",""};
    int found = 0, lineCount = 0, keyFound = 0, keyNum = 0, i = 0, j = 0;
    string tokenDisp, token;
    char n[100];

    //
    ifstream IS ("data.txt", ios::in);
    if  (IS.is_open())  {
        
        while (1)   { // getting line.
            
            IS.getline(n, 100);
            
            while (1) { // TOKENizing string.
                

                if ( n[i] == ' ' || n[i] == '\n') { // selecting token.
                        
                        tokenDisp = word(token); // replacing '_' to ' '.
                        
                        if (lineCount == 0) {
                            keyFound = comp(key,token);
                            if (keyFound == 0)  {
                                //cout<<endl<<token<<endl;
                                keyNum++;
                            }   else    { // this will cut run time.
                                //cout<<endl<<token<<endl;
                                break;
                            }
                        }   else    {

                            found = comp(query,token); // finding the query

                            if (found == 1) {// this will cut run time.
                                
                                break;
                            }
                        }
                        // reset
                        token.clear();
                        tokenDisp.clear();
                        
                }   else    {

                        token = token + n[i];
                }
                
                i++;
            }
            if (lineCount == 0) {
                if (keyFound == 0)  { // did not find key
                    return "query failed : did not find key";
                    
                }
            }
            lineCount++; // lineCounts the number of lines

            // reset
            i = 0;

            //if found
            if (found)  {
                //cout<<n<<endl;
                break;
            }
            // if not found
            if (IS.eof())  {
                //cout<< "sorry we did not find your query"<<endl;
                return "query failed : data not found";
                
            }

        }
    } else  {
        
        return "system error";
    }
    IS.close();
    
    while (n[i] != '\0') { // adding info to nn
        
        if (n[i] == ' ') {
            
            j += 1;
        } else {
            
            nn[j] += n[i];
        }

        i++;
    }
    //cout<<endl<<keyNum<<endl;
    return word(nn[keyNum]);
}

void insertCol()    {
    /* incertCol() inserts column */
    /*  code    */
}

void insert()   {
    /* incertCol() inserts data into file */
    /*  code    */
}

int main(int argc, char* argv[])  {
    
    string query, key, keyToken;
    
    if (argc < 2)   {

        // take the query the flat file.
        cout<<"Please enter a what you key :";
        getline(cin, key);

        cout<<"Please enter what you want to retrieve key from :";
        getline(cin, query);

        keyToken = find(query, key);
        
        cout<<keyToken<<endl;

    }   else    {
        
        key = argv[1];
        query = argv[2];

        keyToken = find(query, key);
        
        cout<<keyToken<<endl;
    
    }

    return 0;
}