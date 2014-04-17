//
//  socketObject.h
//  NetHome
//
//  Created by MACMini1 on 11-10-25.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sys/socket.h>
#import <netinet/in.h>
#import <arpa/inet.h>
#import <unistd.h>

@interface SocketObject : NSObject {
   
    CFSocketRef _socket;
    char NreciveData[9];
    char (*p)[];
    NSString *recivNsstring;
    
    //id *quan;
}

@property (nonatomic)CFSocketRef _socket;
@property (nonatomic)char *reciveData;
@property (nonatomic)char (*p)[];

@property (nonatomic, retain)  NSString *recivNsstring;
+ (SocketObject*)sharedSocketObject;
//@property (nonatomic) id *quan;

//-(id)initWith:(CFSocketRef) _socket1;
//-(void)readStream;
-(int) doConnect:(const char *)severIP;

-(void) sendMessage:(char *)recData;

@end
