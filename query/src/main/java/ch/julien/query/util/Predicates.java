package ch.julien.query.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import ch.julien.common.delegate.Predicate;
import ch.julien.query.core.Query;


/**
 * Common {@link Predicate} implementations for convenience 
 */
public class Predicates {
	
	/**
	 * Select if element is not <code>null</code>
	 */
	public static final Predicate<Object> notNull() {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return arg != null;
			}
		};
	}
	
	/**
	 * Select if collection is not empty (nor <code>null</code>)
	 * 
	 * @see Collection#isEmpty()
	 */
	public static final Predicate<Collection<?>> notEmptyCollection() {
		return new Predicate<Collection<?>>() {
			@Override
			public boolean invoke(Collection<?> arg) {
				return arg != null && ! arg.isEmpty();
			}
		};
	}
	
	/**
	 * Select if map is not empty (nor <code>null</code>)
	 * 
	 * @see Map#isEmpty()
	 */
	public static final Predicate<Map<?, ?>> notEmptyMap() {
		return new Predicate<Map<?,?>>() {
			@Override
			public boolean invoke(Map<?, ?> arg) {
				return arg != null && ! arg.isEmpty();
			}
		};
	}
	
	/**
	 * Select if array is not empty (nor <code>null</code>)
	 */
	public static final <T> Predicate<T[]> notEmptyArray() {
		return new Predicate<T[]>() {
			@Override
			public boolean invoke(T[] arg) {
				return arg != null && arg.length > 0;
			}
		};
	}
	
	/**
	 * Select if element is instance of <code>clazz</code>
	 * 
	 * @see Class#isInstance(Object)
	 */
	public static final Predicate<Object> elementOfInstance(final Class<?> clazz) {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return clazz.isInstance(arg);
			}
		};
	}
	
	/**
	 * Select if class (element) is assignable from <code>clazz</code> (nor <code>null</code>)
	 * 
	 * @see Class#isAssignableFrom(Class)
	 */
	public static final Predicate<Class<?>> elementAssignableFrom(final Class<?> clazz) {
		return new Predicate<Class<?>>() {
			@Override
			public boolean invoke(Class<?> arg) {
				return arg != null && arg.isAssignableFrom(clazz);
			}
		};
	}
	
	/**
	 * Create {@link ExpressionPredicate} for the <code>predicate</code> 
	 */
	public static final <T> ExpressionPredicate<T> that(final Predicate<T> predicate) {
		return new ExpressionPredicate<T>(predicate);
	}
	
	/**
	 * Select if element is not selected by the <code>predicate</code>
	 */
	public static final <T> Predicate<T> not(final Predicate<T> predicate) {
		return new Predicate<T>() {
			@Override
			public boolean invoke(T arg) {
				return ! predicate.invoke(arg);
			}
			@Override
			public String toString() {
				return "not[" + predicate.toString() + "]";
			}
		};
	}
	
	/**
	 * Select if element is selected by all <code>predicates</code>
	 */
	public static final <T> Predicate<T> and(final Predicate<T>... predicates) {
		return new Predicate<T>() {
			@Override
			public boolean invoke(T arg) {
				for (Predicate<T> predicate : predicates) {
					if ( ! predicate.invoke(arg)) {
						return false;
					}
				}
				return true;
			}
			@Override
			public String toString() {
				return "and" + Arrays.asList(predicates).toString();
			}
		};
	}
	
	/**
	 * Select if element is selected by one or more of the <code>predicates</code> 
	 */
	public static final <T> Predicate<T> or(final Predicate<T>... predicates) {
		return new Predicate<T>() {
			@Override
			public boolean invoke(T arg) {
				for (Predicate<T> predicate : predicates) {
					if (predicate.invoke(arg)) {
						return true;
					}
				}
				return false;
			}
			@Override
			public String toString() {
				return "or" + Arrays.asList(predicates).toString();
			}
		};
	}
	
	/**
	 * Select if element is selected by exactly one of the <code>predicates</code>
	 */
	public static final <T> Predicate<T> xor(final Predicate<T>... predicates) {
		return new Predicate<T>() {
			@Override
			public boolean invoke(final T arg) {
				return Query.from(predicates).select(new Predicate<Predicate<T>>() {
					@Override
					public boolean invoke(Predicate<T> arg1) {
						return arg1.invoke(arg);
					}
				}).count() == 1;
			}
			@Override
			public String toString() {
				return "xor" + Arrays.asList(predicates).toString();
			}
		};
	}
	
	/**
	 * Select if string is not empty (nor <code>null</code>)
	 * 
	 * @see String#isEmpty()
	 */
	public static final Predicate<String> notEmptyString() {
		return new Predicate<String>() {
			@Override
			public boolean invoke(String arg) {
				return arg != null && ! arg.isEmpty();
			}
		};
	}
	
	/**
	 * Select if string starts with <code>prefix</code> (and is not <code>null</code>)
	 * 
	 * @see String#startsWith(String)
	 */
	public static final Predicate<String> stringStartingWith(final String prefix) {
		return new Predicate<String>() {
			@Override
			public boolean invoke(String arg) {
				return arg != null && arg.startsWith(prefix);
			}
		};
	}
	
	/**
	 * Select if string ends with <code>suffix</code> (and is not <code>null</code>)
	 * 
	 * @see String#endsWith(String)
	 */
	public static final Predicate<String> stringEndingWith(final String suffix) {
		return new Predicate<String>() {
			@Override
			public boolean invoke(String arg) {
				return arg != null && arg.endsWith(suffix);
			}
		};
	}
	
	/**
	 * Select if string is matching the <code>regex</code> (and is not <code>null</code>)
	 * 
	 * @see String#matches(String)
	 */
	public static final Predicate<String> stringMatching(final String regex) {
		return new Predicate<String>() {
			@Override
			public boolean invoke(String arg) {
				return arg != null && arg.matches(regex);
			}
		};
	}
	
	/*
	 * Predicates only useful for testing
	 */
	
	/**
	 * Always select element
	 */
	static final Predicate<Object> all() {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return true;
			}
			@Override
			public String toString() {
				return "all";
			}
		};
	}

	/**
	 * Never select element
	 */
	static final Predicate<Object> none() {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return false;
			}
			@Override
			public String toString() {
				return "none";
			}
		};
	}
	
}
