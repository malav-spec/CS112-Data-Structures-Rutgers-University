package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		if(!sc.hasNextLine())
		{
			return;
		}
		
		root=new TagNode(tagName(sc.nextLine()),null,null);//The first node "html" of the tree
		
		Stack<TagNode> tags=new Stack<TagNode>();
		tags.push(root);
		String name="";
		while(sc.hasNextLine())//iterates the whole HTML document
		{
		 	name=sc.nextLine();
		 	boolean isTag=false;
		 	
		 	if(name.charAt(0)=='<')
		 	{
		 		name=tagName(name);
		 		isTag=true;
		 	}
		 	
		 	if(isTag && (name.charAt(0)=='/'))
			{
		 		tags.pop();	
		    }
		 	else
		 	{
		 		TagNode element=new TagNode(name,null,null);
		 		if(tags.peek().firstChild==null)
		 		{
		 			tags.peek().firstChild=element;
		 		}
		 		else 
		 		{
		 			TagNode child=tags.peek().firstChild;
		 			
		 			while(child.sibling!=null)
		 			{
		 				child=child.sibling;
		 			}
		 		     
		 			child.sibling=element;
		 		}
		 		
		 		if(isTag)
		 		{
		 			tags.push(element);
		 		}
		 		
		 	}
		 			 			
		}
		
		
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		
		/** COMPLETE THIS METHOD **/
		if(oldTag==null || newTag==null || root==null)
		{
			return;
		}
		else
		{
			this.ReplaceTag(root.firstChild,oldTag,newTag);
		}
			
		
	}
	
	private void ReplaceTag(TagNode Node, String Old, String New )
	{
		if(Node==null)
		{
			return;
		}
		else if(Node.tag.compareTo(Old)==0)
		{
			Node.tag=New;
		}
		
		this.ReplaceTag(Node.firstChild, Old, New);//Inorder traversals
		this.ReplaceTag(Node.sibling, Old, New); 
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		
		TagNode current=new TagNode(null,null,null);
		
		TagNode i;
		
		current=makeBold(root);
		
		if(current==null)
		{
			System.out.println("Table oes not exist");
			return;
		}
		
		current=current.firstChild;
		
		int j=1;//traverse through rows
		while(j<row)
		{
			current=current.sibling;
			j=j+1;
		}
		
		i=current.firstChild;
		while(i!=null)
		{
			i.firstChild=new TagNode("b",i.firstChild,null);
			i=i.sibling;
		}
		
	}
	
	private TagNode makeBold(TagNode temp)
	{
		if(temp==null)
		{
			return null;
		}
		
		TagNode temp1=null;
		String x=temp.tag;
		
		if(x.compareTo("table")==0)
		{
			temp1=temp;
			return temp1;
		}
		
		if(temp1==null)
		{
			temp1=makeBold(temp.sibling);
		}
		
		if(temp1==null)
		{
			temp1=makeBold(temp.firstChild);
		}
		return temp1;
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if((root==null) || (tag==null))
		{
			return;
		}
		else 
		{
			while(hasTag(tag,root))
			{
				remover(tag,root,root.firstChild);
			}
		}
		
	}
	
	private boolean hasTag(String tag, TagNode temp)
	{
		if(temp==null)
		{
			return false;
		}
		else if(temp.tag.equals(tag))
		{
			return true;
		}
		
		boolean tbd=(hasTag(tag,temp.firstChild) || hasTag(tag,temp.sibling));
		
		return tbd;
	}
	
	private void remover(String tag, TagNode head, TagNode temp)
	{
		if(temp== null || head==null)
		{
			return;
		}
		else if(temp.tag.equals(tag))
		{
			if(tag.equals("ol") || tag.equals("ul"))
			{
				TagNode child=temp.firstChild;
				
				while(child!=null)
				{
					if(child.tag.equals("li"))
					{
						child.tag="p";
					}
					
					child=child.sibling;
				}
			}
			
			if(head.firstChild==temp)
			{
				head.firstChild=temp.firstChild;
				TagNode child=temp.firstChild;
				
				while(child.sibling!=null)
				{
					child=child.sibling;
				}
				
				child.sibling=temp.sibling;
			}
			else if(head.sibling==temp)
			{
				TagNode child=temp.firstChild;
				
				while(child.sibling!=null)
				{
					child=child.sibling;
				}
				
				child.sibling=temp.sibling;
				head.sibling=temp.firstChild;
			}
			return;
		}
		
		head=temp;
		remover(tag,head,temp.firstChild);
		remover(tag,head,temp.sibling);

	}
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		
		tagAdder(root, null,word,tag);
	}
	
	private void tagAdder(TagNode curr, TagNode prev, String Word, String Tag)
	{
		if(curr==null)
		{
			return;
		}
		
		if(prev!=null && prev.tag==Tag)
		{
			return;
		}
		
		if(Tag.equals("html") || Tag.equals("body") || Tag.equals("p") || Tag.equals("em") || Tag.equals("tr") || Tag.equals("td") || Tag.equals("ol") || Tag.equals("ul") || Tag.equals("b") || Tag.equals("table") || Tag.equals("li")) 

		{
			if(curr.tag.equals("html") || curr.tag.equals("body") || curr.tag.equals("p") || curr.tag.equals("em") || curr.tag.equals("tr") || curr.tag.equals("td") || curr.tag.equals("ol") || curr.tag.equals("ul") || curr.tag.equals("b") || curr.tag.equals("table") || curr.tag.equals("li"))
			{
				
			}
			else
			{
				String temp[]=curr.tag.split(" ");
				
				int i=temp.length;
				int l=0;
				
				String previous,target,after;
				
				TagNode x=new TagNode(Tag,null,null);
				
				if(l==1) 
				{
					int j;
					for(j=0;j<l;j=j+1) 
					{
						if(Compare(temp[i], Word, null))
						{
							if(prev.firstChild==curr)
							{
								if(curr.sibling!=null)
								{
									prev.firstChild=x;
									x.firstChild=curr;
									x.sibling=curr.sibling;
									curr.sibling=null;
								}
								else
								{
									prev.firstChild=x;
									x.firstChild=curr;
								}
							}
							
							if(prev.sibling==curr)
							{
								if(curr.sibling!=null)
								{
									prev.sibling=x;
									x.firstChild=curr;
									x.sibling=curr.sibling;
									curr.sibling=null;
								}
								else
								{
									prev.sibling=x;
									x.firstChild=curr;
								}
							}
						}
					}
				}
				else
				{
					boolean B=true;
					boolean A=true;
					boolean T=true;
					
					TagNode head=null;
					TagNode last=null;
					
					while(T==true) 
					{
						TagNode temp1=new TagNode(null,null,null);
						TagNode temp2=new TagNode(null,null,null);
						TagNode temp3=new TagNode(null,null,null);
						
						previous="";
						target="";
						after="";
						int c;
						for(c=0;c<l && (A==true);c++) 
						{
							if(Compare(temp[c], Word,null))
							{
								B=false;
								A=false;
								
								String Q=temp[c];
								target=Q;
								
								temp2.tag=target;
								
								int I=l-1;
								
								if(c!=I)
								{
									int p;
									for(p=c+1;p<l;p++)
									{
										after=after+temp[p]+" ";
									}
									
									temp3.tag=after;
								}
							}
							else if(B==true)
							{
								previous=previous+temp[c]+" ";
								temp1.tag=previous;
							}
						}
						
						if(A==true)
						{
							if(prev.firstChild==curr)
							{
								prev.firstChild=temp1;
							}
							
							if(prev.sibling==curr)
							{
								prev.sibling=temp1;
							}
							
							break;
						}
						
						if(temp1.tag!=null && temp2.tag!=null && temp3.tag!=null)
						{
							temp1.sibling=x;
							x.firstChild=temp2;
							x.sibling=temp3;
						}
						else if(temp1.tag!=null && temp2.tag!=null)
						{
							temp1.sibling=x;
							x.firstChild=temp2;
						}
						else if(temp3.tag!=null)
						{
							x.firstChild=temp2;
							x.sibling=temp3;
						}
						
						if(head==null && temp1.tag!=null)
						{
							head=temp1;
						}
						else if(head==null && temp1.tag==null)
						{
							x.firstChild=temp2;
							head=x;
						}
						else
						{
							last=temp2;
						}
						
						if(temp3.tag!=null)
						{
							T=true;
							
							temp=temp3.tag.split(" ");
							
							l=temp.length;
							
							if(head==null && temp1.tag!=null)
							{
								head=temp1;
							}
							else if(head==null && temp1.tag==null)
							{
								x.firstChild=temp2;
								head=x;
							}
							else 
							{
								last=temp3;
							}
						}
						else
						{
							T=false;
						}
					}
					if(prev.firstChild==curr)
					{
						prev.firstChild=head;
					}
					else if(prev.sibling==curr)
					{
						prev.sibling=head;
					}
					
					if(curr.sibling!=null)
					{
						last.sibling=curr.sibling;
						curr.sibling=null;
					}
					
				}
			}
			tagAdder(curr.firstChild,curr,Word,Tag);
			tagAdder(curr.sibling,curr,Word,Tag);
						
		}
	}
	
	private boolean Compare(String curr, String target, TagNode x)
	{
		String d=curr.toLowerCase();
		String e=target.toLowerCase();
		
		int L=curr.length();
		char chr=curr.charAt(L-1);
		
		String s1=d;
		String s2=e;
		
		if(s1.equals(s2))
		{
			return true;
		}
		
		char last=chr;
		String sub=s1.substring(0,curr.length()-1);
		
		if(Character.isLetter(last))
		{
			return false;
		}
		else if(s2.equals(sub))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Helper Methods start
	
	private static String tagName(String name)
	{
		return name.substring(1,name.length()-1);
	}
	
	//Helper method end
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
