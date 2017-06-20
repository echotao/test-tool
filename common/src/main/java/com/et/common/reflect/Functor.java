package com.et.common.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterError;
import org.apache.log.Logger;

/**
 * Created by shatao on 13/6/2017.
 */
public class Functor {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final Object invokee;
    private final Class<?> clazz;
    private final String methodName;
    private Object[] args;
    private final Class<?>[] types;
    private Method methodToInvoke;

    Functor() {
        throw new IllegalArgumentException("Must provide at least one argument");
    }

    public Functor(Object _invokee, String _methodName) {
        this((Class)null, _invokee, _methodName, (Object[])null, (Class[])null);
    }

    public Functor(Class<?> _clazz, String _methodName) {
        this(_clazz, (Object)null, _methodName, (Object[])null, (Class[])null);
    }

    public Functor(Object _invokee, String _methodName, Class<?>[] _types) {
        this((Class)null, _invokee, _methodName, (Object[])null, _types);
    }

    public Functor(Class<?> _clazz, String _methodName, Class<?>[] _types) {
        this(_clazz, (Object)null, _methodName, (Object[])null, _types);
    }

    public Functor(String _methodName) {
        this((Class)null, (Object)null, _methodName, (Object[])null, (Class[])null);
    }

    public Functor(String _methodName, Class<?>[] _types) {
        this((Class)null, (Object)null, _methodName, (Object[])null, _types);
    }

    public Functor(Object _invokee, String _methodName, Object[] _args) {
        this((Class)null, _invokee, _methodName, _args, (Class[])null);
    }

    public Functor(String _methodName, Object[] _args) {
        this((Class)null, (Object)null, _methodName, _args, (Class[])null);
    }

    private Functor(Class<?> _clazz, Object _invokee, String _methodName, Object[] _args, Class<?>[] _types) {
        if(_methodName == null) {
            throw new IllegalArgumentException("Methodname must not be null");
        } else if(_clazz != null && _invokee != null) {
            throw new IllegalArgumentException("Cannot provide both Class and Object");
        } else if(_args != null && _types != null) {
            throw new IllegalArgumentException("Cannot provide both arguments and argument types");
        } else {
            this.clazz = _clazz != null?_clazz:(_invokee != null?_invokee.getClass():null);
            this.invokee = _invokee;
            this.methodName = _methodName;
            this.args = _args;
            this.types = _types != null?_types:(_args != null?_getTypes(_args):null);
        }
    }

    private Object doInvoke(Class<?> _class, Object _invokee, Object[] _args) {
        Class[] argTypes = this.getTypes(_args);

        String message;
        try {
            Method e = this.doCreateMethod(_class, argTypes);
            if(e == null) {
                message = "Can\'t find method " + _class.getName() + "#" + this.methodName + this.typesToString(argTypes);
                log.error(message, new Throwable());
                throw new JMeterError(message);
            } else {
                return e.invoke(_invokee, _args);
            }
        } catch (Exception var7) {
            message = "Trouble functing: " + _class.getName() + "." + this.methodName + "(...) : " + " invokee: " + _invokee + " " + var7.getMessage();
            log.warn(message, var7);
            throw new JMeterError(message, var7);
        }
    }

    public Object invoke() {
        if(this.invokee == null) {
            throw new IllegalStateException("Cannot call invoke() - invokee not known");
        } else {
            return this.doInvoke(this.clazz, this.invokee, this.getArgs());
        }
    }

    public Object invoke(Object p_invokee) {
        return this.invoke(p_invokee, this.getArgs());
    }

    public Object invoke(Object[] p_args) {
        if(this.invokee == null) {
            throw new IllegalStateException("Invokee was not provided in constructor");
        } else {
            return this.doInvoke(this.clazz, this.invokee, this.args != null?this.args:p_args);
        }
    }

    public Object invoke(Object p_invokee, Object[] p_args) {
        return this.doInvoke(this.clazz != null?this.clazz:p_invokee.getClass(), this.invokee != null?this.invokee:p_invokee, this.args != null?this.args:p_args);
    }

    private synchronized Method doCreateMethod(Class<?> p_class, Class<?>[] p_types) {
        if(log.isDebugEnabled()) {
            log.debug("doCreateMethod() using " + this.toString() + "class=" + p_class.getName() + " types: " + Arrays.asList(p_types));
        }

        if(this.methodToInvoke == null) {
            try {
                this.methodToInvoke = p_class.getMethod(this.methodName, p_types);
            } catch (Exception var8) {
                for(int i = 0; i < p_types.length; ++i) {
                    Class primitive = this.getPrimitive(p_types[i]);
                    if(primitive != null) {
                        this.methodToInvoke = this.doCreateMethod(p_class, this.getNewArray(i, primitive, p_types));
                        if(this.methodToInvoke != null) {
                            return this.methodToInvoke;
                        }
                    }

                    Class[] interfaces = p_types[i].getInterfaces();

                    for(int parent = 0; parent < interfaces.length; ++parent) {
                        this.methodToInvoke = this.doCreateMethod(p_class, this.getNewArray(i, interfaces[parent], p_types));
                        if(this.methodToInvoke != null) {
                            return this.methodToInvoke;
                        }
                    }

                    Class var9 = p_types[i].getSuperclass();
                    if(var9 != null) {
                        this.methodToInvoke = this.doCreateMethod(p_class, this.getNewArray(i, var9, p_types));
                        if(this.methodToInvoke != null) {
                            return this.methodToInvoke;
                        }
                    }
                }
            }
        }

        return this.methodToInvoke;
    }

    /** @deprecated */
    @Deprecated
    public boolean checkMethod(Object _invokee) {
        Method m = null;

        try {
            m = this.doCreateMethod(_invokee.getClass(), this.getTypes(this.args));
        } catch (Exception var4) {
            ;
        }

        return m != null;
    }

    /** @deprecated */
    @Deprecated
    public boolean checkMethod(Object _invokee, Class<?> c) {
        Method m = null;

        try {
            m = this.doCreateMethod(_invokee.getClass(), new Class[]{c});
        } catch (Exception var5) {
            ;
        }

        return m != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        if(this.clazz != null) {
            sb.append(this.clazz.getName());
        }

        if(this.invokee != null) {
            sb.append("@");
            sb.append(System.identityHashCode(this.invokee));
        }

        sb.append(".");
        sb.append(this.methodName);
        this.typesToString(sb, this.types);
        return sb.toString();
    }

    private void typesToString(StringBuilder sb, Class<?>[] _types) {
        sb.append("(");
        if(_types != null) {
            for(int i = 0; i < _types.length; ++i) {
                if(i > 0) {
                    sb.append(",");
                }

                sb.append(_types[i].getName());
            }
        }

        sb.append(")");
    }

    private String typesToString(Class<?>[] argTypes) {
        StringBuilder sb = new StringBuilder();
        this.typesToString(sb, argTypes);
        return sb.toString();
    }

    private Class<?> getPrimitive(Class<?> t) {
        return t == null?null:(t.equals(Integer.class)?Integer.TYPE:(t.equals(Long.class)?Long.TYPE:(t.equals(Double.class)?Double.TYPE:(t.equals(Float.class)?Float.TYPE:(t.equals(Byte.class)?Byte.TYPE:(t.equals(Boolean.class)?Boolean.TYPE:(t.equals(Short.class)?Short.TYPE:(t.equals(Character.class)?Character.TYPE:null))))))));
    }

    private Class<?>[] getNewArray(int i, Class<?> replacement, Class<?>[] orig) {
        Class[] newArray = new Class[orig.length];

        for(int j = 0; j < newArray.length; ++j) {
            if(j == i) {
                newArray[j] = replacement;
            } else {
                newArray[j] = orig[j];
            }
        }

        return newArray;
    }

    private Class<?>[] getTypes(Object[] _args) {
        return this.types == null?_getTypes(_args):this.types;
    }

    private static Class<?>[] _getTypes(Object[] _args) {
        Class[] _types;
        if(_args != null) {
            _types = new Class[_args.length];

            for(int i = 0; i < _args.length; ++i) {
                _types[i] = _args[i].getClass();
            }
        } else {
            _types = new Class[0];
        }

        return _types;
    }

    private Object[] getArgs() {
        if(this.args == null) {
            this.args = new Object[0];
        }

        return this.args;
    }
}
