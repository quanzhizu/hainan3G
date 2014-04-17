//
//  socketObject.m
//  NetHome
//
//  Created by MACMini1 on 11-10-25.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "socketObject.h"
#import "SynthesizeSingleton.h"

extern char *serviceIP;

@implementation SocketObject

@synthesize _socket;
@synthesize reciveData;
//@synthesize reciveData;
@synthesize p;
@synthesize recivNsstring;
SYNTHESIZE_SINGLETON_FOR_CLASS(SocketObject);//设置为单例模式
//@synthesize quan;

/*-(id)initWith:(CFSocketRef) _socket1
{
    self._socket = _socket1;
    [super init];
    //self.quan = quan1;
    return self;
    
}*/
-(void)dealloc
{
    
    free(NreciveData);
    [super dealloc];
}

-(id)init
{
    
    [super init];
    NSLog(@"BBBBBBBBBBBBBB");
    return self;
}

-(void)setReciveData:(char [])qreciveData
{
    for (int i=0; i<9; i++) {
        
        NreciveData[i] = *(qreciveData +i);
    }
    
}


-(void)alertview
{
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"打开书柜灯成功" delegate:nil cancelButtonTitle:@"关闭" otherButtonTitles:nil];
    [alert show];
    [alert release];
    
}
-(void)Nalertview
{
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"打开书柜灯失败" delegate:nil cancelButtonTitle:@"关闭" otherButtonTitles:nil];
    [alert show];
    [alert release];
    
}


-(void) readStream{
	char buffer[16];
    memset(buffer, 0, 16);
    
    NSLog(@"RRRRRRREEEEEEAAAAAAAADDDDDDDDSSSSSTTRRRREEEEEAAAAAAMMMMM");
    NSString *s;
	NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
	while (recv(CFSocketGetNative(_socket), buffer, sizeof(buffer), 0)) {
        
        if (buffer[0] == -1) {
            
            [NSThread detachNewThreadSelector:@selector(alertview) toTarget:self withObject:nil];
        }
        else
        {
            [NSThread detachNewThreadSelector:@selector(Nalertview) toTarget:self withObject:nil];
            
        }
        printf("buffer is :");
        //const char *quan = (const char *)buffer[0];
		//s = [NSString stringWithUTF8String:buffer];
        s = [NSString stringWithCString:buffer encoding:NSUTF8StringEncoding];
        NSLog(@"s is : %@",s);
        char string = buffer[0];
        recivNsstring = [[NSString alloc] initWithUTF8String:&string];
        NSLog(@"recivNsstring = %@",recivNsstring);
        
		//[self performSelectorOnMainThread:@selector(setTextInMainThread:) withObject:s waitUntilDone:YES];
	}
    //NSLog(@"%@",s);
    
	[pool release];
}


static void TCPServerConnectCallBack(CFSocketRef socket, CFSocketCallBackType type, CFDataRef address, const void *data, void *info) {
    
    
    NSLog(@"BBBBBBBBBBAAAAAAAAACCCCCCCCLLLLLLLLLCCCCCCCCCAAAAAAAALLLLLLLLL");
	if (data != NULL) {
        
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"回调连接失败" delegate:nil cancelButtonTitle:@"关闭" otherButtonTitles:nil];
		[alert show];
		[alert release];
		return;
	}
    
    
    
	//TCPClientDemoAppDelegate *delegate = (TCPClientDemoAppDelegate *)info;
	//[delegate performSelectorInBackground:@selector(readStream) withObject:nil];
    SocketObject *delegate = (SocketObject *)info;
    //readStream(socket);
    [delegate performSelectorInBackground:@selector(readStream) withObject:nil];
	//[delegate.connController dismissModalViewControllerAnimated:YES];
	//[[NSNotificationCenter defaultCenter] addObserver:delegate.chatController selector:@selector(keyboardWillShown:) name:UIKeyboardWillShowNotification object:nil];
	//[[NSNotificationCenter defaultCenter] addObserver:delegate.chatController selector:@selector(keyboardWillHidden:) name:UIKeyboardWillHideNotification object:nil];
}


- (int) doConnect:(const char *)severIP {
	
    //char *serviceIP = "220.191.160.146";
    CFSocketContext CTX = {0, self, NULL, NULL, NULL};
	 _socket = CFSocketCreate(kCFAllocatorDefault, PF_INET, SOCK_STREAM, IPPROTO_TCP, kCFSocketConnectCallBack, TCPServerConnectCallBack, &CTX);
    
	if (NULL == _socket) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"创建套接字失败" delegate:nil cancelButtonTitle:@"关闭" otherButtonTitles:nil];
		[alert show];
		[alert release];
        NSLog(@"SSSSSSSSSFFFFFFFFFFFFFF");
        return -1;
	}
	struct sockaddr_in addr4;
	memset(&addr4, 0, sizeof(addr4));
	addr4.sin_len = sizeof(addr4);
	addr4.sin_family = AF_INET;
	addr4.sin_port = htons(4196);
	addr4.sin_addr.s_addr = inet_addr(severIP);
	CFDataRef address = CFDataCreate(kCFAllocatorDefault, (UInt8 *)&addr4, sizeof(addr4));
	
	int  i = -1;
    //CFSocketError error;
    i = CFSocketConnectToAddress(_socket, address, 3);
    if (i != 0) {
        return -1;
    }
	
    NSLog(@"CFSocketConnectToAddress is %d",i);
	CFRunLoopRef cfrl = CFRunLoopGetCurrent();
	CFRunLoopSourceRef source = CFSocketCreateRunLoopSource(kCFAllocatorDefault, _socket, 0);
	CFRunLoopAddSource(cfrl, source, kCFRunLoopCommonModes);
	CFRelease(source);
    NSLog(@"DDDDDDDDDDDDDDD");
    return 0;
}



- (void) sendMessage:(char *)recData{
    /*char buf[9];
    buf[0] = 0xFF;
    buf[1] = 0x55;
    buf[2] = 0x03;
    buf[3] = 0x05;
    buf[4] = 0x00;
    buf[5] = 0x01;
    buf[6] = 0x00;
    buf[7] = 0x00;
    buf[8] = 0xEF;
    
    char *buf1 = &buf[0];
    NSString *quan = [NSString stringWithUTF8String:buf1];
    NSLog(@"buf is : %@",quan);*/
    
    
    send(CFSocketGetNative(_socket), NreciveData, sizeof(NreciveData), 0);
    //sen
    
    NSLog(@"SSSSSSSSSSSEEEEEEEEEEEENNNNNNNNNDDDDDDDD");
    return;
    
	/*NSString *stringToSend = chatController.textField.text;
     const char *data = [stringToSend UTF8String];
     //    int temp = GetIntegerFromString(data);
     //    NSLog(@"%d",temp);
     NSMutableData *dataBuffer = [[NSMutableData alloc] initWithData:[stringToSend dataUsingEncoding:NSASCIIStringEncoding]];
     NSLog(@"%@",dataBuffer);
     send(CFSocketGetNative(_socket), dataBuffer, strlen(data) + 1, 0);
     [dataBuffer release];
     //printf("data = %c",data);
     //send(CFSocketGetNative(_socket), data, strlen(data) + 1, 0);
     NSRange endRange;
     endRange.location = [chatController.textView.text length];
     endRange.length = [stringToSend length];
     //	chatController.textView.text = [chatController.textView.text stringByAppendingString:[@"me: " stringByAppendingString:stringToSend]];
     [chatController.textView scrollRangeToVisible:endRange];
     chatController.textField.text = @"";*/
}


@end
