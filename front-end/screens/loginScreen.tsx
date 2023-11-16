import React, { useState } from "react";
import { View, ImageBackground, Text, TextInput, TouchableOpacity, Image } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { StatusBar } from "react-native";
import COLORS from "../constants/colors";
import Button from "../components/Button";

const signin = {
  email: "user9first@email.com",
  password: "user9pass",
  };

  type Token = {
    accessToken: string,
    refreshToken: string
  }
  /**
   * 
   * {"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjlhMjU1OTQ3LTVmMDUtNDViNC05NDBkLTc1NzA5YWRkYmJjNyIsInN1YiI6InVzZXI5IiwiaWF0IjoxNjk4Nzc0NjI2LCJleHAiOjE2OTg4NjEwMjZ9.-xA-54z-nMbjA3IVbyKEcwrulroOxSedmlOgIMBqVdk", "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoIjoidHJ1ZSIsInN1YiI6InVzZXI5IiwiaWF0IjoxNjk4Nzc0NjI2LCJleHAiOjE3MDY1NTA2MjZ9.Ti241RhTjh3fDSeQCzwLuV-Y456it9H1g1p3e4uRWKw"}
   */
  
  
  fetch("http://localhot:8080/api/v1/auth/signin", {
  method: "POST",
  // mode: "no-cors",
  headers: {
  "Content-Type": "application/json",
  },
  body: JSON.stringify(signin),
  })
  .then((res) => res.json())
  .then((_res) => {
    const _tokens = _res as Token;
    console.log(_tokens.accessToken)
    console.log(_tokens.refreshToken)
  // Do SOMETHING
  console.log(_res);
  });
  type ShopperGroup = {
    id: string,
    name: string,
    admin: string,
    users: {
      id: string,
      username: string
    }[],
    lists: {
      id: string,
      name: string
    }[]

  }
  
  // fetch("http:/:8080/api/v1/users/9a255947-5f05-45b4-940d-75709addbbc7/groups", {
  // method: "GET",
  // // mode: "no-cors",
  // headers: {
  // "Content-Type": "application/json",
  // "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjlhMjU1OTQ3LTVmMDUtNDViNC05NDBkLTc1NzA5YWRkYmJjNyIsInN1YiI6InVzZXI5IiwiaWF0IjoxNjk4Nzc1MjY3LCJleHAiOjE2OTg4NjE2Njd9.wjVPw1sAAov3dkII0m4kC0HVDUk07CT0NfMjWmQxt8s",
  // },
  // // body: JSON.stringify(signin),
  // })
  // .then((res) => res.json())
  // .then((_res) => {
  // // Do SOMETHING
  // _res.map((x:any) => x as ShopperGroup)

  // _res.forEach((element: ShopperGroup) => {
  //   console.log(element.admin)
  //   console.log(element.id)
  //   console.log(element.name)
  //   element.users.forEach((u:any) => {console.log(u.username)});
  //   element.lists.forEach((u:any) => {console.log(u.name)});
  // });
  // // console.log(_res);
  // });

  // fetch("http://8080/api/v1/users/9a255947-5f05-45b4-940d-75709addbbc7/groups/26147cc0-9499-4ab7-9a8c-d7de7d42d073/shopping-lists/62dbf4b9-fdfc-4d4c-8135-fbec64e6d83a", {
  // method: "GET",
  // // mode: "no-cors",
  // headers: {
  // "Content-Type": "application/json",
  // "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjlhMjU1OTQ3LTVmMDUtNDViNC05NDBkLTc1NzA5YWRkYmJjNyIsInN1YiI6InVzZXI5IiwiaWF0IjoxNjk4Nzc1MjY3LCJleHAiOjE2OTg4NjE2Njd9.wjVPw1sAAov3dkII0m4kC0HVDUk07CT0NfMjWmQxt8s",
  // },
  // // body: JSON.stringify(signin),
  // })
  // .then((res) => res.json())
  // .then((_res) => {
  // // Do SOMETHING
  // console.log(_res);
  // });
  
interface LoginScreenProps {
  navigation: any;
}

const loginScreen = (props: LoginScreenProps) => {
  const [isPasswordShown, setIsPasswordShown] = useState(false);
  const lists = () => props.navigation.navigate("Lists");
  const home = () => props.navigation.navigate("Welcome");

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: COLORS.white }}>
      <StatusBar barStyle="light-content" />
      <ImageBackground
        style={{ flex: 1, position: "absolute", height: "100%", width: "100%" }}
        source={require("../assets/background.png")}
      />
      <View style={{ flex: 1, marginHorizontal: 18 }}>
        <View>
          <TouchableOpacity onPress={home}>
            <Text style={{ fontSize: 18, color: COLORS.white, fontWeight: "bold", marginLeft: 8 }}>
              Back
            </Text>
          </TouchableOpacity>
        </View>
        <View style={{ justifyContent: "center", top: 100 }}>
          <Text style={{ fontSize: 50, fontWeight: "800", color: COLORS.white, textAlign: "center" }}>
            Welcome Back
          </Text>
        </View>
        <View style={{ top: 350 }}>
          <Text style={{ fontSize: 16, fontWeight: "400", marginVertical: 10 }}>
            Email or Username
          </Text>
          <View
            style={{
              width: "100%",
              height: 60,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: "center",
              justifyContent: "center",
              padding: 22,
            }}
          >
            <TextInput
              placeholder="Enter your username or email"
              placeholderTextColor={COLORS.black}
              style={{ width: "100%", fontSize: 15 }}
            />
          </View>
        </View>
        <View style={{ marginBottom: 12, marginTop: 380 }}>
          <Text style={{ fontSize: 16, fontWeight: "400", marginVertical: 10 }}>
            Password
          </Text>
          <View
            style={{
              width: "100%",
              height: 60,
              borderColor: COLORS.black,
              borderWidth: 1,
              borderRadius: 8,
              alignItems: "center",
              justifyContent: "center",
              padding: 22,
            }}
          >
            <TextInput
              placeholder="Enter your password"
              placeholderTextColor={COLORS.black}
              secureTextEntry={isPasswordShown}
              style={{ width: "100%", fontSize: 15 }}
            />
            <TouchableOpacity
              onPress={() => setIsPasswordShown(!isPasswordShown)}
              style={{ position: "absolute", right: 12 }}
            >
              {isPasswordShown ? (
                <Image
                  source={require("../assets/eye2.png")}
                  style={{
                    height: 33,
                    resizeMode: "contain",
                    width: 40,
                    borderRadius: 20,
                    position: "absolute",
                    top: -20,
                    right: -5,
                  }}
                />
              ) : (
                <Image
                  source={require("../assets/eye.png")}
                  style={{
                    height: 33,
                    resizeMode: "contain",
                    width: 40,
                    borderRadius: 20,
                    position: "absolute",
                    top: -20,
                    right: -5,
                  }}
                />
              )}
            </TouchableOpacity>
          </View>
        </View>
        <Button title="Login" filled onPress={lists} style={{ marginTop: 20, marginBottom: 4 }}/>
        <View>
        <Image
          style={{ top: -4, width: 445, height: 300, alignItems: "center", justifyContent: "center" }}
          resizeMode="cover"
          source={require("../assets/loginpic2.jpg")}
        />
        </View>
      </View>
    </SafeAreaView>
  );
};

export default loginScreen;
